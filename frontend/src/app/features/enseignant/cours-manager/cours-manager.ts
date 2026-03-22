import { Component, OnInit } from '@angular/core';
import { EnseignantService } from '../../../core/services/enseignant.service';
import { CoursService } from '../../../core/services/cours.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Cours, Matiere } from '../../../core/models/models';

@Component({
  selector: 'app-cours-manager',
  standalone: false,
  templateUrl: './cours-manager.html',
  styleUrl: './cours-manager.scss',
})
export class CoursManager implements OnInit {
  coursList: Cours[] = [];
  matieres: Matiere[] = [];
  isLoading = true;
  showForm = false;
  editingCours: Cours | null = null;

  form: any = {
    matiereId: null, titre: '', chapitre: '', numeroChapitre: 1,
    contenu: '', resume: '', type: 'COURS', niveau: 'TERMINAL', dureeLecture: 30
  };

  types = ['COURS', 'RESUME', 'FICHE'];
  niveaux = ['PREMIERE', 'TERMINAL'];
  displayedColumns = ['titre', 'matiere', 'chapitre', 'type', 'actions'];

  constructor(
    private enseignantService: EnseignantService,
    private coursService: CoursService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.coursService.getMatieres().subscribe(m => this.matieres = m);
    this.loadCours();
  }

  loadCours(): void {
    this.isLoading = true;
    this.coursService.getAllCours().subscribe({
      next: (list) => { this.coursList = list; this.isLoading = false; },
      error: () => { this.isLoading = false; }
    });
  }

  openCreateForm(): void {
    this.editingCours = null;
    this.form = { matiereId: null, titre: '', chapitre: '', numeroChapitre: 1, contenu: '', resume: '', type: 'COURS', niveau: 'TERMINAL', dureeLecture: 30 };
    this.showForm = true;
  }

  openEditForm(cours: Cours): void {
    this.editingCours = cours;
    this.form = {
      matiereId: cours.matiere.id, titre: cours.titre, chapitre: cours.chapitre,
      numeroChapitre: cours.numeroChapitre, contenu: cours.contenu, resume: cours.resume,
      type: cours.type, niveau: cours.niveau, dureeLecture: cours.dureeLecture
    };
    this.showForm = true;
  }

  saveCours(): void {
    if (this.editingCours) {
      this.enseignantService.updateCours(this.editingCours.id, this.form).subscribe({
        next: () => { this.snackBar.open('Cours mis à jour', 'OK', { duration: 2000 }); this.showForm = false; this.coursService.clearCache(); this.loadCours(); },
        error: (e) => this.snackBar.open(e.error?.error || 'Erreur', 'OK', { duration: 3000 })
      });
    } else {
      this.enseignantService.createCours(this.form).subscribe({
        next: () => { this.snackBar.open('Cours créé', 'OK', { duration: 2000 }); this.showForm = false; this.coursService.clearCache(); this.loadCours(); },
        error: (e) => this.snackBar.open(e.error?.error || 'Erreur', 'OK', { duration: 3000 })
      });
    }
  }

  deleteCours(cours: Cours): void {
    if (!confirm(`Supprimer "${cours.titre}" ?`)) return;
    this.enseignantService.deleteCours(cours.id).subscribe({
      next: () => { this.snackBar.open('Cours supprimé', 'OK', { duration: 2000 }); this.coursService.clearCache(); this.loadCours(); },
      error: () => this.snackBar.open('Erreur lors de la suppression', 'OK', { duration: 3000 })
    });
  }
}