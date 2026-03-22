import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

interface NavItem {
  label: string;
  icon: string;
  route: string;
  roles?: string[]; // if undefined = all roles
}

@Component({
  selector: 'app-sidebar',
  standalone: false,
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class Sidebar {
  allNavItems: NavItem[] = [
    { label: 'Tableau de bord', icon: 'dashboard', route: '/dashboard' },
    { label: 'Cours', icon: 'menu_book', route: '/cours', roles: ['ETUDIANT', 'ENSEIGNANT', 'ADMIN'] },
    { label: 'Exercices', icon: 'assignment', route: '/exercices', roles: ['ETUDIANT', 'ENSEIGNANT', 'ADMIN'] },
    { label: 'BacIA Chat', icon: 'smart_toy', route: '/chat', roles: ['ETUDIANT'] },
    { label: 'Planning', icon: 'calendar_today', route: '/planning', roles: ['ETUDIANT'] },
    // Enseignant
    { label: 'Gérer les Cours', icon: 'edit_note', route: '/enseignant/cours', roles: ['ENSEIGNANT', 'ADMIN'] },
    { label: 'Gérer les Exercices', icon: 'rule', route: '/enseignant/exercices', roles: ['ENSEIGNANT', 'ADMIN'] },
    // Admin
    { label: 'Utilisateurs', icon: 'manage_accounts', route: '/admin/users', roles: ['ADMIN'] },
  ];

  constructor(public router: Router, public authService: AuthService) {}

  get navItems(): NavItem[] {
    const role = this.authService.currentUser?.role || 'ETUDIANT';
    return this.allNavItems.filter(item => !item.roles || item.roles.includes(role));
  }

  isActive(route: string): boolean {
    return this.router.url === route || this.router.url.startsWith(route + '/');
  }

  navigate(route: string): void {
    this.router.navigate([route]);
  }
}