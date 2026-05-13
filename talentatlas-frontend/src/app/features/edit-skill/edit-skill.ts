import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { SkillService, SkillRequest } from '../../core/skills';
import { AppNav } from '../../core/app-nav/app-nav';

@Component({
  selector: 'app-edit-skill',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, AppNav],
  templateUrl: './edit-skill.html',
  styleUrl: './edit-skill.css',
})
export class EditSkill implements OnInit {
  private skillService = inject(SkillService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  id = 0;
  name = '';
  category = '';
  proficiency = 1;

  loading = true;
  saving = false;
  errorMessage = '';
  successMessage = '';

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      this.errorMessage = 'Skill ID is missing.';
      this.loading = false;
      return;
    }

    this.id = Number(idParam);

    this.skillService.getSkillById(this.id).subscribe({
      next: (skill) => {
        this.name = skill.name;
        this.category = skill.category;
        this.proficiency = skill.proficiency;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load skill.';
        this.loading = false;
      },
    });
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';
    this.saving = true;

    const request: SkillRequest = {
      name: this.name,
      category: this.category,
      proficiency: this.proficiency,
    };

    this.skillService.updateSkill(this.id, request).subscribe({
      next: () => {
        this.successMessage = 'Skill updated successfully. Redirecting...';
        this.router.navigate(['/skills']);
      },
      error: () => {
        this.errorMessage = 'Failed to update skill.';
        this.saving = false;
      },
    });
  }
}
