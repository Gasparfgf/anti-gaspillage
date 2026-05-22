package backend.antigasp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import backend.antigasp.dto.request.ReservationRequest;
import backend.antigasp.dto.response.ReservationResponse;
import backend.antigasp.model.enumsBusiness.ReservationStatus;
import backend.antigasp.service.ReservationService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationRequest request,
            Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.createReservation(request, principal.getName()));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(Principal principal) {
        return ResponseEntity.ok(reservationService.getMyReservations(principal.getName()));
    }

    @GetMapping("/offer/{offerId}")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<List<ReservationResponse>> getReservationsByOffer(
            @PathVariable Long offerId,
            Principal principal) {
        return ResponseEntity.ok(
                reservationService.getReservationsByOffer(offerId, principal.getName())
        );
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<ReservationResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam ReservationStatus status,
            Principal principal) {
        return ResponseEntity.ok(
                reservationService.updateStatus(id, status, principal.getName())
        );
    }
}
