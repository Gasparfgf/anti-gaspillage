package backend.antigasp.service;

import com.invendus.dto.request.OfferRequest;
import com.invendus.dto.response.OfferResponse;
import com.invendus.model.Offer;
import com.invendus.model.User;
import com.invendus.repository.OfferRepository;
import com.invendus.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {

    private static final Logger log = LoggerFactory.getLogger(OfferService.class);

    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

    public List<OfferResponse> getAllOffers() {
        return offerRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public OfferResponse getOfferById(Long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Offer not found with id : " + id));
        return toResponse(offer);
    }

    public List<OfferResponse> getMyOffers(String email) {
        User merchant = getUserByEmail(email);
        return offerRepository.findByMerchantId(merchant.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    public OfferResponse createOffer(OfferRequest request, String email) {
        User merchant = getUserByEmail(email);

        Offer offer = Offer.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .merchant(merchant)
                .build();

        Offer saved = offerRepository.save(offer);
        log.info("Offer [id={}] created by the merchant [email={}]", saved.getId(), email);
        return toResponse(saved);
    }

    public OfferResponse updateOffer(Long id, OfferRequest request, String email) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Offer not found with ID : " + id));

        checkOwnership(offer, email, "update");

        offer.setTitle(request.getTitle());
        offer.setDescription(request.getDescription());
        offer.setPrice(request.getPrice());

        Offer updated = offerRepository.save(offer);
        log.info("Offer [id={}] updated by [email={}]", id, email);
        return toResponse(updated);
    }

    public void deleteOffer(Long id, String email) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Offer not found with ID : " + id));

        checkOwnership(offer, email, "delete");

        offerRepository.delete(offer);
        log.info("Offer [id={}] deleted by [email={}]", id, email);
    }

    private void checkOwnership(Offer offer, String email, String action) {
        if (!offer.getMerchant().getEmail().equals(email)) {
            log.warn("Access denied: [email={}] attempted to {} offer [id={}]",
                    email, action, offer.getId());
            throw new AccessDeniedException(
                    "You are not authorized to " + action + " this offer"
            );
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found with email : " + email
                ));
    }

    private OfferResponse toResponse(Offer offer) {
        return OfferResponse.builder()
                .id(offer.getId())
                .title(offer.getTitle())
                .description(offer.getDescription())
                .price(offer.getPrice())
                .merchantId(offer.getMerchant().getId())
                .merchantName(offer.getMerchant().getFirstname()
                        + " " + offer.getMerchant().getSurname())
                .build();
    }
}