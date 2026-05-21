package backend.antigasp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import backend.antigasp.dto.request.OfferRequest;
import backend.antigasp.dto.response.OfferResponse;
import backend.antigasp.service.OfferService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @GetMapping
    public ResponseEntity<List<OfferResponse>> getAllOffers() {
        return ResponseEntity.ok(offerService.getAllOffers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferResponse> getOfferById(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.getOfferById(id));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<List<OfferResponse>> getMyOffers(Principal principal) {
        return ResponseEntity.ok(offerService.getMyOffers(principal.getName()));
    }

    @PostMapping
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<OfferResponse> createOffer(@Valid @RequestBody OfferRequest request,
                                                     Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(offerService.createOffer(request, principal.getName()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<OfferResponse> updateOffer(@PathVariable Long id,
                                                     @Valid @RequestBody OfferRequest request,
                                                     Principal principal) {
        return ResponseEntity.ok(offerService.updateOffer(id, request, principal.getName()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MERCHANT') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id, Principal principal) {
        offerService.deleteOffer(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}