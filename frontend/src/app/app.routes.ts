import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
    // Auth
    {
    path: 'login',
    loadComponent: () =>
        import('./features/auth/login/login.component').then(m => m.LoginComponent)
    },
    {
    path: 'register',
    loadComponent: () =>
        import('./features/auth/register/register.component').then(m => m.RegisterComponent)
    },

    // Public
    {
    path: 'offers',
    loadComponent: () =>
        import('./features/offers/offers-list/offers-list').then(m => m.OffersList)
    },

    // Client
    {
    path: 'my-reservations',
    canActivate: [authGuard, roleGuard(['CLIENT'])],
    loadComponent: () =>
        import('./features/reservations/my-reservations/my-reservations')
        .then(m => m.MyReservations)
    },

    // Merchant
    {
    path: 'merchant',
    canActivate: [authGuard, roleGuard(['MERCHANT'])],
    loadComponent: () =>
        import('./features/merchant/merchant-dashboard/merchant-dashboard')
        .then(m => m.MerchantDashboard)
    },

    // Admin
    {
    path: 'admin',
    canActivate: [authGuard, roleGuard(['ADMIN'])],
    loadComponent: () =>
        import('./features/admin/admin-dashboard/admin-dashboard')
        .then(m => m.AdminDashboard)
    },

    // Fallback
    { path: 'unauthorized', redirectTo: '/offers' },
    { path: '', redirectTo: '/offers', pathMatch: 'full' },
    { path: '**', redirectTo: '/offers' }
];