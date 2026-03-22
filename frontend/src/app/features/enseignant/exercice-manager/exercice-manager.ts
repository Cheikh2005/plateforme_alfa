import { Component, OnInit } from '@angular/core';
import { EnseignantService } from '../../../core/services/enseignant.service';
import { ExerciceService } from '../../../core/services/exercice.service';
import { CoursService } from '../../../core/services/cours.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Exercice, Matiere } from '../../../core/models/models';

@Component({
  selector: 'app-exercice-manager',
  standalone: false,
  templateUrl: './exercice-manager.html',
  styleUrl: './exercice-manager.scss',
})
export class ExerciceManager implements OnInit {
  exercicesList: Exercice[] = [];
  matieres: Matiere[] = [];
  isLoading = true;
  showForm = false;
  editingExercice: Exercice | null = null;

  form: any = {
    matiereId: null, titre: '', enonce: '', correction: '',
    difficulte: 'MOYEN', type: 'EXERCICE', anneeBac: null,
    dureeMinutes: 20, pointsTotal: 20
  };

  difficultes = ['FACILE', 'MOYEN', 'DIFFICILE'];
  types = ['EXERCICE', 'QCM', 'ANNALE'];
  displayedColumns = ['titre', 'matiere', 'type', 'difficulte', 'actions'];

  constructor(
    private enseignantService: EnseignantService,
    private exerciceService: ExerciceService,
    private coursService: CoursService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.coursService.getMatieres().subscribe(m => this.matieres = m);
    this.loadExercices();
  }

  loadExercices(): void {
    this.isLoading = true;
    this.exerciceService.getAllExercices().subscribe({
      next: (list) => { this.exercicesList = list; this.isLoading = false; },
      error: () => { this.isLoading = false; }
    });
  }

  openCreateForm(): void {
    this.editingExercice = null;
    this.form = { matiereId: null, titre: '', enonce: '', correction: '', difficulte: 'MOYEN', type: 'EXERCICE', anneeBac: null, dureeMinutes: 20, pointsTotal: 20 };
    this.showForm = true;
  }

  openEditForm(ex: Exercice): void {
    this.editingExercice = ex;
    this.form = {
      matiereId: ex.matiere.id, titre: ex.titre, enonce: ex.enonce,
      correction: ex.correction, difficulte: ex.difficulte, type: ex.type,
      anneeBac: ex.anneeBac, dureeMinutes: ex.dureeMinutes, pointsTotal: ex.pointsTotal
    };
    this.showForm = true;
  }

  saveExercice(): void {
    if (this.editingExercice) {
      this.enseignantService.updateExercice(this.editingExercice.id, this.form).subscribe({
        next: () => { this.snackBar.open('Exercice mis à jour', 'OK', { duration: 2000 }); this.showForm = false; this.exerciceService.clearCache(); this.loadExercices(); },
        error: (e) => this.snackBar.open(e.error?.error || 'Erreur', 'OK', { duration: 3000 })
      });
    } else {
      this.enseignantService.createExercice(this.form).subscribe({
        next: () => { this.snackBar.open('Exercice créé', 'OK', { duration: 2000 }); this.showForm = false; this.exerciceService.clearCache(); this.loadExercices(); },
        error: (e) => this.snackBar.open(e.error?.error || 'Erreur', 'OK', { duration: 3000 })
      });
    }
  }

  deleteExercice(ex: Exercice): void {
    if (!confirm(`Supprimer "${ex.titre}" ?`)) return;
    this.enseignantService.deleteExercice(ex.id).subscribe({
      next: () => { this.snackBar.open('Exercice supprimé', 'OK', { duration: 2000 }); this.exerciceService.clearCache(); this.loadExercices(); },
      error: () => this.snackBar.open('Erreur lors de la suppression', 'OK', { duration: 3000 })
    });
  }

  getDifficulteColor(diff: string): string {
    if (diff === 'FACILE') return '#2e7d32';
    if (diff === 'MOYEN') return '#e65100';
    return '#c62828';
  }
}