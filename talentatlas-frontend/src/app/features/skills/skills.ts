import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SkillService, SkillResponse } from '../../core/skills';
import { AppNav } from '../../core/app-nav/app-nav';

@Component({
  selector: 'app-skills',
  standalone: true,
  imports: [CommonModule, RouterModule, AppNav],
  templateUrl: './skills.html',
  styleUrl: './skills.css',
})
export class Skills implements OnInit {
  private skillService = inject(SkillService);

  skills: SkillResponse[] = [];
  loading = true;
  errorMessage = '';

  ngOnInit(): void {
    this.skillService.getSkills().subscribe({
      next: (data) => {
        this.skills = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load skills:', error);
        this.errorMessage = 'Failed to load skills.';
        this.loading = false;
      },
    });
  }

  deleteSkill(skill: SkillResponse): void {
    if (!confirm(`Delete skill "${skill.name}"?`)) {
      return;
    }

    this.skillService.deleteSkill(skill.id).subscribe({
      next: () => {
        this.skills = this.skills.filter((s) => s.id !== skill.id);
      },
      error: (error) => {
        console.error('Failed to delete skill:', error);
        this.errorMessage = 'Failed to delete skill.';
      },
    });
  }
}
