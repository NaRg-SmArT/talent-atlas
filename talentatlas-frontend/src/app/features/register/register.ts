import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { environment } from '../../core/environment';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  fullName = '';
  email = '';
  password = '';

  loading = false;
  successMessage = '';
  errorMessage = '';
  fieldErrors: Record<string, string> = {};

  private http = inject(HttpClient);
  private router = inject(Router);
  private apiUrl = `${environment.apiBaseUrl}/users`;

  onSubmit(): void {
    this.loading = true;
    this.successMessage = '';
    this.errorMessage = '';
    this.fieldErrors = {};

    const requestBody = {
      fullName: this.fullName,
      email: this.email,
      password: this.password,
    };

    this.http.post(this.apiUrl, requestBody).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Registration successful. Redirecting to login...';

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1200);
      },
      error: (error) => {
        this.loading = false;
        this.fieldErrors = error?.error?.validationErrors ?? {};

        if (error.status === 400 && Object.keys(this.fieldErrors).length > 0) {
          this.errorMessage = 'Please correct the highlighted fields.';
        } else if (error.status === 409) {
          this.errorMessage =
            error?.error?.message ?? 'An account with that email already exists.';
        } else {
          this.errorMessage =
            error?.error?.message ??
            'Registration failed. Please check your input and try again.';
        }
      },
    });
  }
}
