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

import backend.antigasp.dto.request.OfferRequest;
import backend.antigasp.dto.response.OfferResponse;
import backend.antigasp.model.entity.Offer;
import backend.antigasp.model.entity.User;
import backend.antigasp.model.enumsBusiness.Role;
import backend.antigasp.repository.OfferRepository;
import backend.antigasp.repository.UserRepository;
import backend.antigasp.service.OfferService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OfferService offerService;

    private User merchant;
    private User otherMerchant;
    private Offer offer;
    private OfferRequest offerRequest;

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
                .id(2L)
                .firstname("Marie")
                .surname("Martin")
                .email("marie@example.com")
                .role(Role.MERCHANT)
                .build();

        offer = Offer.builder()
                .id(1L)
                .title("Unsold sandwichs")
                .description("5 end-of-day sandwichs")
                .price(3.50)
                .merchant(merchant)
                .build();

        offerRequest = new OfferRequest();
        offerRequest.setTitle("Unsold sandwichs");
        offerRequest.setDescription("5 end-of-day sandwichs");
        offerRequest.setPrice(3.50);
    }

    @Test
    @DisplayName("createOffer — Nominal case: the offer is created and returned")
    void createOffer_shouldReturnOfferResponse_whenMerchantExists() {
        // Arrange
        when(userRepository.findByEmail("jean@example.com")).thenReturn(Optional.of(merchant));
        when(offerRepository.save(any(Offer.class))).thenReturn(offer);

        // Act
        OfferResponse response = offerService.createOffer(offerRequest, "jean@example.com");

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Unsold sandwichs");
        assertThat(response.getPrice()).isEqualTo(3.50);
        assertThat(response.getMerchantId()).isEqualTo(1L);
        verify(offerRepository, times(1)).save(any(Offer.class));
    }

    @Test
    @DisplayName("createOffer — Error: User not found -> EntityNotFoundException")
    void createOffer_shouldThrowEntityNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("inconnu@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> offerService.createOffer(offerRequest, "inconnu@example.com"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(offerRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOffer — Nominal case: the offer is updated")
    void updateOffer_shouldReturnUpdatedOffer_whenOwnerUpdates() {
        // Arrange
        Offer updatedOffer = Offer.builder()
                .id(1L)
                .title("New title")
                .description("New description")
                .price(5.00)
                .merchant(merchant)
                .build();

        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));
        when(offerRepository.save(any(Offer.class))).thenReturn(updatedOffer);

        offerRequest.setTitle("New title");
        offerRequest.setDescription("New description");
        offerRequest.setPrice(5.00);

        // Act
        OfferResponse response = offerService.updateOffer(1L, offerRequest, "jean@example.com");

        // Assert
        assertThat(response.getTitle()).isEqualTo("New title");
        assertThat(response.getPrice()).isEqualTo(5.00);
        verify(offerRepository, times(1)).save(any(Offer.class));
    }

    @Test
    @DisplayName("updateOffer — Error: Not owned -> AccessDeniedException")
    void updateOffer_shouldThrowAccessDeniedException_whenNotOwner() {
        // Arrange
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        // Act & Assert
        assertThatThrownBy(() ->
                offerService.updateOffer(1L, offerRequest, "marie@example.com"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("authorized");

        verify(offerRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOffer — Error: Offer not found -> EntityNotFoundException")
    void updateOffer_shouldThrowEntityNotFoundException_whenOfferNotFound() {
        // Arrange
        when(offerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() ->
                offerService.updateOffer(99L, offerRequest, "jean@example.com"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Offer not found");
    }

    @Test
    @DisplayName("deleteOffer — nominal case: the offer is withdrawn")
    void deleteOffer_shouldDeleteOffer_whenOwnerDeletes() {
        // Arrange
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        // Act
        offerService.deleteOffer(1L, "jean@example.com");

        // Assert
        verify(offerRepository, times(1)).delete(offer);
    }

    @Test
    @DisplayName("deleteOffer — error: not owned -> AccessDeniedException")
    void deleteOffer_shouldThrowAccessDeniedException_whenNotOwner() {
        // Arrange
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        // Act & Assert
        assertThatThrownBy(() ->
                offerService.deleteOffer(1L, "marie@example.com"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("authorized");

        verify(offerRepository, never()).delete(any());
    }
}
