import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatListModule } from '@angular/material/list';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatBadgeModule } from '@angular/material/badge';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatRadioModule } from '@angular/material/radio';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatStepperModule } from '@angular/material/stepper';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatTableModule } from '@angular/material/table';
import { TextFieldModule } from '@angular/cdk/text-field';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { JwtInterceptor } from './core/interceptors/jwt.interceptor';

import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { Dashboard } from './features/dashboard/dashboard';
import { Cours } from './features/cours/cours';
import { CoursDetail } from './features/cours/cours-detail/cours-detail';
import { Exercices } from './features/exercices/exercices';
import { ExerciceDetail } from './features/exercices/exercice-detail/exercice-detail';
import { AiChat } from './features/ai-chat/ai-chat';
import { Planning } from './features/planning/planning';
import { Navbar } from './shared/navbar/navbar';
import { Sidebar } from './shared/sidebar/sidebar';
import { AdminUsers } from './features/admin/admin-users/admin-users';
import { CoursManager } from './features/enseignant/cours-manager/cours-manager';
import { ExerciceManager } from './features/enseignant/exercice-manager/exercice-manager';

@NgModule({
  declarations: [
    App, Login, Register, Dashboard, Cours, CoursDetail,
    Exercices, ExerciceDetail, AiChat, Planning, Navbar, Sidebar,
    AdminUsers, CoursManager, ExerciceManager
  ],
  imports: [
    BrowserModule, BrowserAnimationsModule, AppRoutingModule,
    HttpClientModule, FormsModule, ReactiveFormsModule,
    MatToolbarModule, MatSidenavModule, MatButtonModule, MatIconModule,
    MatCardModule, MatInputModule, MatFormFieldModule, MatListModule,
    MatChipsModule, MatProgressBarModule, MatProgressSpinnerModule,
    MatSelectModule, MatTabsModule, MatDialogModule, MatSnackBarModule,
    MatBadgeModule, MatTooltipModule, MatMenuModule, MatDividerModule,
    MatCheckboxModule, MatExpansionModule, MatRadioModule,
    MatDatepickerModule, MatNativeDateModule, MatStepperModule,
    MatSlideToggleModule, MatTableModule, TextFieldModule
  ],
  providers: [
    provideBrowserGlobalErrorListeners(),
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ],
  bootstrap: [App]
})
export class AppModule { }