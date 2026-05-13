import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import {
  ApplicationsService,
  CreateApplicationRequest,
} from '../../core/applications';
import { AppNav } from '../../core/app-nav/app-nav';

@Component({
  selector: 'app-edit-application',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, AppNav],
  templateUrl: './edit-application.html',
  styleUrl: './edit-application.css',
})
export class EditApplication implements OnInit {
  applicationId = 0;

  companyName = '';
  positionTitle = '';
  jobPostingUrl = '';
  primaryContactName = '';
  primaryContactPhone = '';
  primaryContactEmail = '';
  status = 'APPLIED';
  appliedAt = '';
  notes = '';

  loading = true;
  saving = false;
  successMessage = '';
  errorMessage = '';

  private applicationsService = inject(ApplicationsService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      this.errorMessage = 'Application id was not provided.';
      this.loading = false;
      return;
    }

    this.applicationId = Number(idParam);

    this.applicationsService.getApplicationById(this.applicationId).subscribe({
      next: (application) => {
        this.companyName = application.companyName;
        this.positionTitle = application.positionTitle;
        this.jobPostingUrl = application.jobPostingUrl ?? '';
        this.primaryContactName = application.primaryContactName ?? '';
        this.primaryContactPhone = application.primaryContactPhone ?? '';
        this.primaryContactEmail = application.primaryContactEmail ?? '';
        this.status = application.status;
        this.appliedAt = application.appliedAt
          ? application.appliedAt.slice(0, 10)
          : '';
        this.notes = application.notes ?? '';
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load application:', error);
        this.errorMessage = 'Failed to load application.';
        this.loading = false;
      },
    });
  }

  onSubmit(): void {
    this.successMessage = '';
    this.errorMessage = '';
    this.saving = true;

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

    this.applicationsService
      .updateApplication(this.applicationId, requestBody)
      .subscribe({
        next: () => {
          this.saving = false;
          this.successMessage =
            'Application updated successfully. Redirecting...';

          setTimeout(() => {
            this.router.navigate(['/applications']);
          }, 1200);
        },
        error: (error) => {
          console.error('Failed to update application:', error);
          this.saving = false;
          this.errorMessage =
            'Failed to update application. Please check your input and try again.';
        },
      });
  }
}
