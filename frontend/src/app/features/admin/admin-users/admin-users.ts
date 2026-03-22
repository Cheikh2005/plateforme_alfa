import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../core/services/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-admin-users',
  standalone: false,
  templateUrl: './admin-users.html',
  styleUrl: './admin-users.scss',
})
export class AdminUsers implements OnInit {
  users: any[] = [];
  isLoading = true;
  showForm = false;
  editingUser: any = null;

  form: any = {
    username: '', email: '', password: '', nom: '', prenom: '', serie: 'C', role: 'ETUDIANT'
  };

  roles = ['ETUDIANT', 'ENSEIGNANT', 'ADMIN'];
  displayedColumns = ['nom', 'username', 'email', 'role', 'actif', 'actions'];

  constructor(private adminService: AdminService, private snackBar: MatSnackBar) {}

  ngOnInit(): void { this.loadUsers(); }

  loadUsers(): void {
    this.isLoading = true;
    this.adminService.getUsers().subscribe({
      next: (users) => { this.users = users; this.isLoading = false; },
      error: () => { this.isLoading = false; }
    });
  }

  openCreateForm(): void {
    this.editingUser = null;
    this.form = { username: '', email: '', password: '', nom: '', prenom: '', serie: 'C', role: 'ETUDIANT' };
    this.showForm = true;
  }

  openEditForm(user: any): void {
    this.editingUser = user;
    this.form = { nom: user.nom, prenom: user.prenom, serie: user.serie, role: user.role, actif: user.actif, password: '' };
    this.showForm = true;
  }

  saveUser(): void {
    if (this.editingUser) {
      this.adminService.updateUser(this.editingUser.id, this.form).subscribe({
        next: () => { this.snackBar.open('Utilisateur mis à jour', 'OK', { duration: 2000 }); this.showForm = false; this.loadUsers(); },
        error: (e) => this.snackBar.open(e.error?.error || 'Erreur', 'OK', { duration: 3000 })
      });
    } else {
      this.adminService.createUser(this.form).subscribe({
        next: () => { this.snackBar.open('Utilisateur créé', 'OK', { duration: 2000 }); this.showForm = false; this.loadUsers(); },
        error: (e) => this.snackBar.open(e.error?.error || 'Erreur', 'OK', { duration: 3000 })
      });
    }
  }

  deleteUser(user: any): void {
    if (!confirm(`Supprimer ${user.username} ?`)) return;
    this.adminService.deleteUser(user.id).subscribe({
      next: () => { this.snackBar.open('Supprimé', 'OK', { duration: 2000 }); this.loadUsers(); },
      error: () => this.snackBar.open('Erreur lors de la suppression', 'OK', { duration: 3000 })
    });
  }

  toggleActif(user: any): void {
    this.adminService.updateUser(user.id, { actif: !user.actif }).subscribe({
      next: () => { this.snackBar.open('Statut modifié', 'OK', { duration: 2000 }); this.loadUsers(); }
    });
  }

  getRoleColor(role: string): string {
    if (role === 'ADMIN') return '#c62828';
    if (role === 'ENSEIGNANT') return '#1565c0';
    return '#2e7d32';
  }

  getRoleLabel(role: string): string {
    if (role === 'ADMIN') return 'Administrateur';
    if (role === 'ENSEIGNANT') return 'Enseignant';
    return 'Étudiant';
  }
}