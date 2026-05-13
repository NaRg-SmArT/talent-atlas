import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../core/auth';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  email = '';
  password = '';

  loading = false;
  errorMessage = '';
  fieldErrors: Record<string, string> = {};

  private authService = inject(AuthService);
  private router = inject(Router);

  onSubmit(): void {
    this.errorMessage = '';
    this.fieldErrors = {};
    this.loading = true;

    this.authService.login(this.email, this.password).subscribe({
      next: (response) => {
        this.authService.saveTokens(response.accessToken, response.refreshToken);
        this.loading = false;
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        this.loading = false;
        this.fieldErrors = error?.error?.validationErrors ?? {};

        if (error.status === 400 && Object.keys(this.fieldErrors).length > 0) {
          this.errorMessage = 'Please correct the highlighted fields.';
        } else if (error.status === 401) {
          this.errorMessage = 'Invalid email or password.';
        } else {
          this.errorMessage =
            error?.error?.message ?? 'Login failed. Please try again.';
        }
      },
    });
  }
}








