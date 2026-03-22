import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class EnseignantGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}
  canActivate(): boolean {
    const role = this.authService.currentUser?.role;
    if (role === 'ENSEIGNANT' || role === 'ADMIN') return true;
    this.router.navigate(['/dashboard']);
    return false;
  }
}