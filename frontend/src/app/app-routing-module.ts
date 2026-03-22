import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { AdminGuard } from './core/guards/admin.guard';
import { EnseignantGuard } from './core/guards/enseignant.guard';

import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { Dashboard } from './features/dashboard/dashboard';
import { Cours } from './features/cours/cours';
import { CoursDetail } from './features/cours/cours-detail/cours-detail';
import { Exercices } from './features/exercices/exercices';
import { ExerciceDetail } from './features/exercices/exercice-detail/exercice-detail';
import { AiChat } from './features/ai-chat/ai-chat';
import { Planning } from './features/planning/planning';
import { AdminUsers } from './features/admin/admin-users/admin-users';
import { CoursManager } from './features/enseignant/cours-manager/cours-manager';
import { ExerciceManager } from './features/enseignant/exercice-manager/exercice-manager';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'dashboard', component: Dashboard, canActivate: [AuthGuard] },
  { path: 'cours', component: Cours, canActivate: [AuthGuard] },
  { path: 'cours/:id', component: CoursDetail, canActivate: [AuthGuard] },
  { path: 'exercices', component: Exercices, canActivate: [AuthGuard] },
  { path: 'exercices/:id', component: ExerciceDetail, canActivate: [AuthGuard] },
  { path: 'chat', component: AiChat, canActivate: [AuthGuard] },
  { path: 'planning', component: Planning, canActivate: [AuthGuard] },
  // Admin
  { path: 'admin/users', component: AdminUsers, canActivate: [AuthGuard, AdminGuard] },
  // Enseignant
  { path: 'enseignant/cours', component: CoursManager, canActivate: [AuthGuard, EnseignantGuard] },
  { path: 'enseignant/exercices', component: ExerciceManager, canActivate: [AuthGuard, EnseignantGuard] },
  { path: '**', redirectTo: '/dashboard' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }