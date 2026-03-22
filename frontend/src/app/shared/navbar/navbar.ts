import { Component, Input } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar {
  @Input() sidenav!: MatSidenav;

  constructor(public authService: AuthService, private router: Router) {}

  get userName(): string {
    const user = this.authService.currentUser;
    return user ? `${user.prenom} ${user.nom}` : '';
  }

  getInitials(): string {
    const user = this.authService.currentUser;
    if (!user) return '?';
    return `${user.prenom.charAt(0)}${user.nom.charAt(0)}`.toUpperCase();
  }

  logout(): void {
    this.authService.logout();
  }

  navigateTo(path: string): void {
    this.router.navigate([path]);
  }
}
