import { Routes } from '@angular/router';
import { LandingPage } from './features/landing-page/landing-page';
import { Login } from './features/login/login';
import { Register } from './features/register/register';
import { Dashboard } from './features/dashboard/dashboard';
import { Applications } from './features/applications/applications';
import { CreateApplication } from './features/create-application/create-application';
import { EditApplication } from './features/edit-application/edit-application';
import { ApplicationDetail } from './features/application-detail/application-detail';
import { Skills } from './features/skills/skills';
import { AddSkill } from './features/add-skill/add-skill';
import { EditSkill } from './features/edit-skill/edit-skill';
import { KeywordAnalysis } from './features/keyword-analysis/keyword-analysis';
import { authGuard } from './core/auth-guard';


export const routes: Routes = [
  { path: '', component: LandingPage },

  { path: 'login', component: Login },
  { path: 'register', component: Register },

  { path: 'dashboard', component: Dashboard, canActivate: [authGuard] },

  { path: 'applications', component: Applications, canActivate: [authGuard] },
  { path: 'applications/new', component: CreateApplication, canActivate: [authGuard] },
  { path: 'applications/:id/edit', component: EditApplication, canActivate: [authGuard] },
  { path: 'applications/:id', component: ApplicationDetail, canActivate: [authGuard] },

  { path: 'skills', component: Skills, canActivate: [authGuard] },
  { path: 'skills/new', component: AddSkill, canActivate: [authGuard] },
  { path: 'skills/:id/edit', component: EditSkill, canActivate: [authGuard] },
  { path: 'analysis', component: KeywordAnalysis, canActivate: [authGuard] },
  { path: '**', redirectTo: '' },
];
