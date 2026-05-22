package backend.antigasp.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import backend.antigasp.dto.request.ReservationRequest;
import backend.antigasp.dto.response.ReservationResponse;
import backend.antigasp.model.entity.Offer;
import backend.antigasp.model.entity.Reservation;
import backend.antigasp.model.entity.User;
import backend.antigasp.model.enumsBusiness.ReservationStatus;
import backend.antigasp.repository.OfferRepository;
import backend.antigasp.repository.ReservationRepository;
import backend.antigasp.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

    public ReservationResponse createReservation(ReservationRequest request, String email) {
        User user = getUserByEmail(email);

        Offer offer = offerRepository.findById(request.getOfferId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Offer not found with ID : " + request.getOfferId()
                ));

        // A merchant cannot reserve their own offer
        if (offer.getMerchant().getEmail().equals(email)) {
            throw new IllegalArgumentException("You cannot book your own offer");
        }

        Reservation reservation = Reservation.builder()
                .user(user)
                .offer(offer)
                .status(ReservationStatus.RESERVED)
                .build();

        Reservation saved = reservationRepository.save(reservation);
        log.info("Reservation created [id={}] by [email={}] on offer [id={}]",
                saved.getId(), email, offer.getId());
        return toResponse(saved);
    }

    public List<ReservationResponse> getMyReservations(String email) {
        User user = getUserByEmail(email);
        return reservationRepository.findByUserId(user.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ReservationResponse> getReservationsByOffer(Long offerId, String email) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Offer not found with ID : " + offerId
                ));

        // Only the merchant owner can see the bookings for their offer.
        if (!offer.getMerchant().getEmail().equals(email)) {
            throw new AccessDeniedException(
                    "You are not authorized to view the bookings for this offer"
            );
        }

        return reservationRepository.findByOfferId(offerId).stream()
                .map(this::toResponse)
                .toList();
    }

    public ReservationResponse updateStatus(Long id, ReservationStatus newStatus, String email) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Reservation not found with ID : " + id
                ));

        // Only the merchant of the offer in question can change the status
        if (!reservation.getOffer().getMerchant().getEmail().equals(email)) {
            throw new AccessDeniedException(
                    "You are not authorized to modify this reservation"
            );
        }

        // Check the status transition : only RESERVED -> PICKED_UP
        if (reservation.getStatus() == ReservationStatus.PICKED_UP) {
            throw new IllegalArgumentException(
                    "This reservation has already been claimed; the status can no longer be changed."
            );
        }

        reservation.setStatus(newStatus);
        Reservation updated = reservationRepository.save(reservation);
        log.info("Booking status [id={}] updated : {} -> {} by [email={}]",
                id, ReservationStatus.RESERVED, newStatus, email);
        return toResponse(updated);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found with email : " + email
                ));
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .offerId(reservation.getOffer().getId())
                .offerTitle(reservation.getOffer().getTitle())
                .userId(reservation.getUser().getId())
                .userName(reservation.getUser().getFirstname()
                        + " " + reservation.getUser().getSurname())
                .status(reservation.getStatus())
                .build();
    }
}
