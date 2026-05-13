import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AppNav } from '../../core/app-nav/app-nav';
import { SkillRequest, SkillService } from '../../core/skills';
import {
  KeywordDictionaryEntry,
  KeywordDictionaryService,
} from '../../core/keyword-dictionary';

@Component({
  selector: 'app-add-skill',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, AppNav],
  templateUrl: './add-skill.html',
  styleUrl: './add-skill.css',
})
export class AddSkill {
  private skillService = inject(SkillService);
  private router = inject(Router);
  private dictionaryService = inject(KeywordDictionaryService);

  name = '';
  category = '';
  proficiency = 1;

  saving = false;
  successMessage = '';
  errorMessage = '';
  fieldErrors: Record<string, string> = {};

  suggestions: KeywordDictionaryEntry[] = [];
  showSuggestions = false;

  onNameInput(value: string): void {
    this.name = value;
    const trimmed = value.trim();

    if (!trimmed) {
      this.suggestions = [];
      this.showSuggestions = false;
      return;
    }

    this.dictionaryService.search(trimmed).subscribe((entries) => {
      this.suggestions = entries;
      this.showSuggestions = entries.length > 0;
    });
  }

  selectSuggestion(entry: KeywordDictionaryEntry): void {
    this.name = entry.name;
    this.category = entry.category;
    this.showSuggestions = false;
  }

  onBlurName(): void {
    setTimeout(() => {
      this.showSuggestions = false;
    }, 150);
  }

  onSubmit(): void {
    this.successMessage = '';
    this.errorMessage = '';
    this.fieldErrors = {};
    this.saving = true;

    const requestBody: SkillRequest = {
      name: this.name,
      category: this.category,
      proficiency: this.proficiency,
    };

    this.skillService.createSkill(requestBody).subscribe({
      next: () => {
        this.saving = false;
        this.successMessage = 'Skill added successfully. Redirecting...';
        setTimeout(() => this.router.navigate(['skills']), 1200);
      },
      error: (error) => {
        this.saving = false;
        this.fieldErrors = error?.error?.validationErrors ?? {};

        if (error.status === 400 && Object.keys(this.fieldErrors).length > 0) {
          this.errorMessage = 'Please correct the highlighted fields.';
        } else {
          this.errorMessage =
            error?.error?.message ??
            'Failed to add skill. Please check your input and try again.';
        }
      },
    });
  }
}
