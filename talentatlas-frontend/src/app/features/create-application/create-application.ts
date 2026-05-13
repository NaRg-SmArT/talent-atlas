import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import {
  ApplicationsService,
  CreateApplicationRequest,
} from '../../core/applications';
import { AppNav } from '../../core/app-nav/app-nav';

@Component({
  selector: 'app-create-application',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, AppNav],
  templateUrl: './create-application.html',
  styleUrl: './create-application.css',
})
export class CreateApplication {
  companyName = '';
  positionTitle = '';
  jobPostingUrl = '';
  primaryContactName = '';
  primaryContactPhone = '';
  primaryContactEmail = '';
  status = 'APPLIED';
  appliedAt = '';
  notes = '';

  loading = false;
  successMessage = '';
  errorMessage = '';
  fieldErrors: Record<string, string> = {};

  private applicationsService = inject(ApplicationsService);
  private router = inject(Router);

  onSubmit(): void {
    this.successMessage = '';
    this.errorMessage = '';
    this.fieldErrors = {};
    this.loading = true;

    const requestBody: CreateApplicationRequest = {
      companyName: this.companyName,
      positionTitle: this.positionTitle,
      jobPostingUrl: this.jobPostingUrl || null,
      primaryContactName: this.primaryContactName || null,
      primaryContactPhone: this.primaryContactPhone || null,
      primaryContactEmail: this.primaryContactEmail || null,
      status: this.status,
      appliedAt: this.appliedAt ? `${this.appliedAt}T00:00:00` : null,
      notes: this.notes || null,
    };

    this.applicationsService.createApplication(requestBody).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Application created successfully. Redirecting...';

        setTimeout(() => {
          this.router.navigate(['/applications']);
        }, 1200);
      },
      error: (error) => {
        this.loading = false;
        this.fieldErrors = error?.error?.validationErrors ?? {};

        if (error.status === 400 && Object.keys(this.fieldErrors).length > 0) {
          this.errorMessage = 'Please correct the highlighted fields.';
        } else {
          this.errorMessage =
            error?.error?.message ??
            'Failed to create application. Please check your input and try again.';
        }
      },
    });
  }
}
