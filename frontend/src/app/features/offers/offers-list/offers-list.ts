import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { OfferService } from '../../../core/services/offer.service';
import { ReservationService } from '../../../core/services/reservation.service';
import { Offer } from '../../../shared/models/offer.model';

@Component({
  selector: 'app-offers-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './offers-list.html',
})
export class OffersListComponent implements OnInit {

  private offerService = inject(OfferService);
  private reservationService = inject(ReservationService);
  private authService = inject(AuthService);
  private router = inject(Router);

  offers: Offer[] = [];
  loading = true;
  errorMessage = '';
  reservingId: number | null = null;
  successMessage = '';

  ngOnInit(): void {
    this.loadOffers();
  }

  loadOffers(): void {
    this.loading = true;
    this.offerService.getAll().subscribe({
      next: (offers) => {
        this.offers = offers;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger les offres.';
        this.loading = false;
      }
    });
  }

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  isClient(): boolean {
    return this.authService.getRole() === 'CLIENT';
  }

  reserve(offerId: number): void {
    if (!this.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }

    this.reservingId = offerId;
    this.successMessage = '';
    this.errorMessage = '';

    this.reservationService.create({ offerId }).subscribe({
      next: () => {
        this.reservingId = null;
        this.successMessage = 'Réservation confirmée !';
      },
      error: (err) => {
        this.reservingId = null;
        this.errorMessage = err.error?.message || 'Impossible de réserver cette offre.';
      }
    });
  }
}