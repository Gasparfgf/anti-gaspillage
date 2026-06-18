import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Role } from '../../shared/models/user.model';

export interface AuthResponse {
    token: string;
    userId: number;
    email: string;
    firstname: string;
    role: Role;
}

export interface RegisterRequest {
    firstname: string;
    surname: string;
    address: string;
    birthDate: string;
    email: string;
    password: string;
}

export interface LoginRequest {
    email: string;
    password: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {

    private http = inject(HttpClient);
    private router = inject(Router);

    private apiUrl = `${environment.apiUrl}/auth`;
    private currentUserSubject = new BehaviorSubject<AuthResponse | null>(this.loadUser());

    currentUser$ = this.currentUserSubject.asObservable();

    register(data: RegisterRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/register`, data).pipe(
            tap(res => this.saveUser(res))
        );
    }

    login(data: LoginRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/login`, data).pipe(
            tap(res => this.saveUser(res))
        );
    }

    logout(): void {
        localStorage.removeItem('auth');
        this.currentUserSubject.next(null);
        this.router.navigate(['/login']);
    }

    getToken(): string | null {
        return this.currentUserSubject.value?.token ?? null;
    }

    getRole(): Role | null {
        return this.currentUserSubject.value?.role ?? null;
    }

    getCurrentUser(): AuthResponse | null {
        return this.currentUserSubject.value;
    }

    isLoggedIn(): boolean {
        return !!this.currentUserSubject.value;
    }

    private saveUser(user: AuthResponse): void {
        localStorage.setItem('auth', JSON.stringify(user));
        this.currentUserSubject.next(user);
    }

    private loadUser(): AuthResponse | null {
        const stored = localStorage.getItem('auth');
        return stored ? JSON.parse(stored) : null;
    }
}