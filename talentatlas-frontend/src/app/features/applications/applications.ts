import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import {
  ApplicationsService,
  ApplicationRecord,
} from '../../core/applications';
import { AppNav } from '../../core/app-nav/app-nav';

@Component({
  selector: 'app-applications',
  standalone: true,
  imports: [CommonModule, RouterLink, AppNav],
  templateUrl: './applications.html',
  styleUrl: './applications.css',
})
export class Applications implements OnInit {
  private applicationsService = inject(ApplicationsService);

  applications: ApplicationRecord[] = [];
  loading = true;
  errorMessage = '';

  ngOnInit(): void {
    this.applicationsService.getApplications().subscribe({
      next: (data) => {
        this.applications = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load applications:', error);
        this.errorMessage = 'Failed to load applications.';
        this.loading = false;
      },
    });
  }

  deleteApplication(application: ApplicationRecord): void {
    const confirmed = window.confirm(
      `Are you sure you want to delete the application for ${application.positionTitle} at ${application.companyName}?`
    );

    if (!confirmed) {
      return;
    }

    this.applicationsService.deleteApplication(application.id).subscribe({
      next: () => {
        this.applications = this.applications.filter(
          (a) => a.id !== application.id
        );
      },
      error: (error) => {
        console.error('Failed to delete application:', error);
        this.errorMessage = 'Failed to delete application.';
      },
    });
  }
}