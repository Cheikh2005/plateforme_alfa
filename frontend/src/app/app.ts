import { Component } from '@angular/core';
import { AuthService } from './core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  standalone: false,
  styleUrl: './app.scss'
})
export class App {
  constructor(public authService: AuthService, public router: Router) {}

  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn;
  }
}
