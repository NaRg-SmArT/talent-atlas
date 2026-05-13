import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AppNav } from '../../core/app-nav/app-nav';

@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [CommonModule, AppNav],
  templateUrl: './landing-page.html',
  styleUrl: './landing-page.css',
})
export class LandingPage {}
