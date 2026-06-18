import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { User } from '../../shared/models/user.model';

@Injectable({ providedIn: 'root' })
export class UserService {

    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/users`;

    getMe(): Observable<User> {
        return this.http.get<User>(`${this.apiUrl}/me`);
    }

    getAll(): Observable<User[]> {
        return this.http.get<User[]>(this.apiUrl);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}