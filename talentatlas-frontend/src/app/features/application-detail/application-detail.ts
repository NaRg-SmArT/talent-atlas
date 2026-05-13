import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import {
  ApplicationsService,
  ApplicationRecord,
} from '../../core/applications';
import { AppNav } from '../../core/app-nav/app-nav';

@Component({
  selector: 'app-application-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, AppNav],
  templateUrl: './application-detail.html',
  styleUrl: './application-detail.css',
})
export class ApplicationDetail implements OnInit {
  private route = inject(ActivatedRoute);
  private applicationsService = inject(ApplicationsService);

  application: ApplicationRecord | null = null;
  loading = true;
  errorMessage = '';

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      this.errorMessage = 'Application id was not provided.';
      this.loading = false;
      return;
    }

    const applicationId = Number(idParam);

    this.applicationsService.getApplicationById(applicationId).subscribe({
      next: (data) => {
        this.application = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load application details:', error);
        this.errorMessage = 'Failed to load application details.';
        this.loading = false;
      },
    });
  }
}
