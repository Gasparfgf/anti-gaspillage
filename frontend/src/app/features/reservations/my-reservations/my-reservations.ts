import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ReservationService } from '../../../core/services/reservation.service';
import { Reservation } from '../../../shared/models/reservation.model';

@Component({
  selector: 'app-my-reservations',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-reservations.html',
})
export class MyReservationsComponent implements OnInit {

  private reservationService = inject(ReservationService);

  reservations: Reservation[] = [];
  loading = true;
  errorMessage = '';

  ngOnInit(): void {
    this.loadReservations();
  }

  loadReservations(): void {
    this.loading = true;
    this.reservationService.getMy().subscribe({
      next: (reservations) => {
        this.reservations = reservations;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger vos réservations.';
        this.loading = false;
      }
    });
  }
}