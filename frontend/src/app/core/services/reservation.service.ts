import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Reservation, ReservationRequest, ReservationStatus } from '../../shared/models/reservation.model';

@Injectable({ providedIn: 'root' })
export class ReservationService {

    private apiUrl = `${environment.apiUrl}/reservations`;

    constructor(private http: HttpClient) {}

    create(request: ReservationRequest): Observable<Reservation> {
        return this.http.post<Reservation>(this.apiUrl, request);
    }

    getMy(): Observable<Reservation[]> {
        return this.http.get<Reservation[]>(`${this.apiUrl}/my`);
    }

    getByOffer(offerId: number): Observable<Reservation[]> {
        return this.http.get<Reservation[]>(`${this.apiUrl}/offer/${offerId}`);
    }

    updateStatus(id: number, status: ReservationStatus): Observable<Reservation> {
        return this.http.patch<Reservation>(
            `${this.apiUrl}/${id}/status`,
            null,
            { params: { status } }
        );
    }
}