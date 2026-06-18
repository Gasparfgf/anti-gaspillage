import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    templateUrl: './register.component.html'
})
export class RegisterComponent {

    private fb = inject(FormBuilder);
    private authService = inject(AuthService);
    private router = inject(Router);

    form: FormGroup;
    errorMessage = '';
    loading = false;

    constructor() {
        this.form = this.fb.group({
            firstname: ['', Validators.required],
            surname:   ['', Validators.required],
            address:   ['', Validators.required],
            birthDate: ['', Validators.required],
            email:     ['', [Validators.required, Validators.email]],
            password:  ['', [Validators.required, Validators.minLength(8)]]
        });
    }

    get firstname() { return this.form.get('firstname')!; }
    get surname()   { return this.form.get('surname')!; }
    get address()   { return this.form.get('address')!; }
    get birthDate() { return this.form.get('birthDate')!; }
    get email()     { return this.form.get('email')!; }
    get password()  { return this.form.get('password')!; }

    onSubmit(): void {
        if (this.form.invalid) return;

        this.loading = true;
        this.errorMessage = '';

        this.authService.register(this.form.value).subscribe({
            next: () => {
                this.loading = false;
                this.router.navigate(['/login']);
            },
            error: (err) => {
                this.loading = false;
                this.errorMessage = err.error?.message || 'Une erreur est survenue';
            }
        });
    }
}