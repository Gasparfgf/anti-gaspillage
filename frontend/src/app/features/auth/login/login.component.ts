import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    templateUrl: './login.component.html'
})
export class LoginComponent {

    form: FormGroup;
    errorMessage = '';
    loading = false;

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router
    ) {
    this.form = this.fb.group({
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(8)]]
    });
    }

    get email() { return this.form.get('email')!; }
    get password() { return this.form.get('password')!; }

    onSubmit(): void {
        if (this.form.invalid) return;

        this.loading = true;
        this.errorMessage = '';

        this.authService.login(this.form.value).subscribe({
            next: (res) => {
                this.loading = false;
                this.redirectByRole(res.role);
            },
            error: (err) => {
                this.loading = false;
                this.errorMessage = err.error?.message || 'Email ou mot de passe incorrect';
            }
        });
    }

    private redirectByRole(role: string): void {
        switch (role) {
            case 'ADMIN':    this.router.navigate(['/admin']);    break;
            case 'MERCHANT': this.router.navigate(['/merchant']); break;
            default:         this.router.navigate(['/offers']);   break;
        }
    }
}