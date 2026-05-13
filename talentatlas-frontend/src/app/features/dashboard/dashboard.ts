import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import {
  ApplicationsService,
  ApplicationRecord,
} from '../../core/applications';
import { AuthService } from '../../core/auth';
import { SkillService, SkillResponse } from '../../core/skills';
import { AppNav } from '../../core/app-nav/app-nav';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, AppNav],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);
  private applicationsService = inject(ApplicationsService);
  private skillService = inject(SkillService);

  applications: ApplicationRecord[] = [];
  skills: SkillResponse[] = [];
  loading = true;
  errorMessage = '';
  reportGeneratedAt = new Date();

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loading = true;
    this.errorMessage = '';

    forkJoin({
      applications: this.applicationsService.getApplications(),
      skills: this.skillService.getSkills(),
    }).subscribe({
      next: ({ applications, skills }) => {
        this.applications = applications;
        this.skills = skills;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load dashboard data.';
        this.loading = false;
      },
    });
  }

  get activeApplications(): ApplicationRecord[] {
    return this.applications.filter(
      (application) =>
        application.status !== 'REJECTED' &&
        application.status !== 'WITHDRAWN' &&
        application.status !== 'ACCEPTED'
    );
  }

  get activeApplicationsCount(): number {
    return this.applications.filter(
      (application) =>
        application.status !== 'REJECTED' &&
        application.status !== 'WITHDRAWN' &&
        application.status !== 'ACCEPTED'
    ).length;
  }

  get interviewCount(): number {
    return this.applications.filter(
      (application) => application.status === 'INTERVIEW'
    ).length;
  }

  get offerCount(): number {
    return this.applications.filter(
      (application) => application.status === 'OFFER'
    ).length;
  }

  get acceptedCount(): number {
    return this.applications.filter(
      (application) => application.status === 'ACCEPTED'
    ).length;
  }

  get savedCount(): number {
    return this.applications.filter(
      (application) => application.status === 'SAVED'
    ).length;
  }

  get appliedCount(): number {
    return this.applications.filter(
      (application) => application.status === 'APPLIED'
    ).length;
  }

  get screeningCount(): number {
    return this.applications.filter(
      (application) => application.status === 'SCREENING'
    ).length;
  }

  get rejectedCount(): number {
    return this.applications.filter(
      (application) => application.status === 'REJECTED'
    ).length;
  }

  get withdrawnCount(): number {
    return this.applications.filter(
      (application) => application.status === 'WITHDRAWN'
    ).length;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}