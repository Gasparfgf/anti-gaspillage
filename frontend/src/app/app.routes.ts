import { Routes } from '@angular/router';

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
    /*{
    path: 'offers',
    loadComponent: () =>
        import('./features/offers/offers-list/offers-list.component').then(m => m.OffersListComponent)
    },

    // Client
    {
    path: 'my-reservations',
    canActivate: [authGuard, roleGuard(['CLIENT'])],
    loadComponent: () =>
        import('./features/reservations/my-reservations/my-reservations.component')
        .then(m => m.MyReservationsComponent)
    },

    // Merchant
    {
    path: 'merchant',
    canActivate: [authGuard, roleGuard(['MERCHANT'])],
    loadComponent: () =>
        import('./features/merchant/merchant-dashboard/merchant-dashboard.component')
        .then(m => m.MerchantDashboardComponent)
    },

    // Admin
    {
    path: 'admin',
    canActivate: [authGuard, roleGuard(['ADMIN'])],
    loadComponent: () =>
        import('./features/admin/admin-dashboard/admin-dashboard.component')
        .then(m => m.AdminDashboardComponent)
    },*/

    // Fallback
    { path: 'unauthorized', redirectTo: '/offers' },
    { path: '', redirectTo: '/offers', pathMatch: 'full' },
    { path: '**', redirectTo: '/offers' }
];