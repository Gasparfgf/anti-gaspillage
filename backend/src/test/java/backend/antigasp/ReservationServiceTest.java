package backend.antigasp;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import backend.antigasp.dto.request.ReservationRequest;
import backend.antigasp.dto.response.ReservationResponse;
import backend.antigasp.model.entity.Offer;
import backend.antigasp.model.entity.Reservation;
import backend.antigasp.model.entity.User;
import backend.antigasp.model.enumsBusiness.ReservationStatus;
import backend.antigasp.model.enumsBusiness.Role;
import backend.antigasp.repository.OfferRepository;
import backend.antigasp.repository.ReservationRepository;
import backend.antigasp.repository.UserRepository;
import backend.antigasp.service.ReservationService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    private User client;
    private User merchant;
    private User otherMerchant;
    private Offer offer;
    private Reservation reservation;
    private ReservationRequest reservationRequest;

    @BeforeEach
    void setUp() {
        merchant = User.builder()
                .id(1L)
                .firstname("Jean")
                .surname("Dupont")
                .email("jean@example.com")
                .role(Role.MERCHANT)
                .build();

        otherMerchant = User.builder()
                .id(3L)
                .firstname("Paul")
                .surname("Leroy")
                .email("paul@example.com")
                .role(Role.MERCHANT)
                .build();

        client = User.builder()
                .id(2L)
                .firstname("Marie")
                .surname("Martin")
                .email("marie@example.com")
                .role(Role.CLIENT)
                .build();

        offer = Offer.builder()
                .id(1L)
                .title("Unsold sandwichs")
                .description("5 end-of-day sandwiches")
                .price(3.50)
                .merchant(merchant)
                .build();

        reservation = Reservation.builder()
                .id(1L)
                .user(client)
                .offer(offer)
                .status(ReservationStatus.RESERVED)
                .build();

        reservationRequest = new ReservationRequest();
        reservationRequest.setOfferId(1L);
    }

    @Test
    @DisplayName("createReservation — nominal case: the reservation is created with the status RESERVED")
    void createReservation_shouldReturnReservationResponse_whenValid() {
        // Arrange
        when(userRepository.findByEmail("marie@example.com")).thenReturn(Optional.of(client));
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        ReservationResponse response = reservationService.createReservation(
                reservationRequest, "marie@example.com"
        );

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(ReservationStatus.RESERVED);
        assertThat(response.getOfferTitle()).isEqualTo("Unsold sandwichs");
        assertThat(response.getUserName()).isEqualTo("Marie Martin");
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("createReservation — erreur : Offer not found -> EntityNotFoundException")
    void createReservation_shouldThrowEntityNotFoundException_whenOfferNotFound() {
        // Arrange
        when(userRepository.findByEmail("marie@example.com")).thenReturn(Optional.of(client));
        when(offerRepository.findById(99L)).thenReturn(Optional.empty());

        reservationRequest.setOfferId(99L);

        // Act & Assert
        assertThatThrownBy(() ->
                reservationService.createReservation(reservationRequest, "marie@example.com"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Offer not found");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("createReservation — error : merchant reserves his own offer -> IllegalArgumentException")
    void createReservation_shouldThrowIllegalArgumentException_whenMerchantReservesOwnOffer() {
        // Arrange
        when(userRepository.findByEmail("jean@example.com")).thenReturn(Optional.of(merchant));
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        // Act & Assert
        assertThatThrownBy(() ->
                reservationService.createReservation(reservationRequest, "jean@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("own offer");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateStatus — Nominal case: status updated to PICKED_UP")
    void updateStatus_shouldUpdateStatus_whenMerchantOwnsOffer() {
        // Arrange
        Reservation updated = Reservation.builder()
                .id(1L)
                .user(client)
                .offer(offer)
                .status(ReservationStatus.PICKED_UP)
                .build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(updated);

        // Act
        ReservationResponse response = reservationService.updateStatus(
                1L, ReservationStatus.PICKED_UP, "jean@example.com"
        );

        // Assert
        assertThat(response.getStatus()).isEqualTo(ReservationStatus.PICKED_UP);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("updateStatus — erreur : not the owner of the offer -> AccessDeniedException")
    void updateStatus_shouldThrowAccessDeniedException_whenNotOfferOwner() {
        // Arrange
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // Act & Assert
        assertThatThrownBy(() ->
                reservationService.updateStatus(1L, ReservationStatus.PICKED_UP, "paul@example.com"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("authorized");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateStatus — error : Reservation already PICKED_UP -> IllegalArgumentException")
    void updateStatus_shouldThrowIllegalArgumentException_whenAlreadyPickedUp() {
        // Arrange
        Reservation alreadyPickedUp = Reservation.builder()
                .id(1L)
                .user(client)
                .offer(offer)
                .status(ReservationStatus.PICKED_UP)
                .build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(alreadyPickedUp));

        // Act & Assert
        assertThatThrownBy(() ->
                reservationService.updateStatus(1L, ReservationStatus.PICKED_UP, "jean@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already been claimed");

        verify(reservationRepository, never()).save(any());
    }
}
