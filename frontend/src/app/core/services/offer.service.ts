import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Offer, OfferRequest } from '../../shared/models/offer.model';

@Injectable({ providedIn: 'root' })
export class OfferService {

    private apiUrl = `${environment.apiUrl}/offers`;

    constructor(private http: HttpClient) {}

    getAll(): Observable<Offer[]> {
        return this.http.get<Offer[]>(this.apiUrl);
    }

    getById(id: number): Observable<Offer> {
        return this.http.get<Offer>(`${this.apiUrl}/${id}`);
    }

    getMy(): Observable<Offer[]> {
        return this.http.get<Offer[]>(`${this.apiUrl}/my`);
    }

    create(request: OfferRequest): Observable<Offer> {
        return this.http.post<Offer>(this.apiUrl, request);
    }

    update(id: number, request: OfferRequest): Observable<Offer> {
        return this.http.put<Offer>(`${this.apiUrl}/${id}`, request);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}