import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AppNav } from '../../core/app-nav/app-nav';
import { AnalysisService, KeywordAnalysisResponse } from '../../core/analysis';


@Component({
  selector: 'app-keyword-analysis',
  standalone: true,
  imports: [CommonModule, FormsModule, AppNav],
  templateUrl: './keyword-analysis.html',
  styleUrl: './keyword-analysis.css'
})
export class KeywordAnalysis {
  jobDescriptionText = '';
  loading = false;
  errorMessage = '';
  result: KeywordAnalysisResponse | null = null;

  private analysisService = inject(AnalysisService);

  onSubmit(): void {
    this.errorMessage = '';
    this.result = null;

    if (!this.jobDescriptionText.trim()) {
      this.errorMessage = 'Please paste a job description before analyzing.';
      return;
    }

    this.loading = true;

    this.analysisService.analyzeKeywords({
      jobDescriptionText: this.jobDescriptionText
    }).subscribe({
      next: (response) => {
        this.result = response;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to analyze keyword gaps. Please try again.';
        this.loading = false;
      }
    });
  }
}
