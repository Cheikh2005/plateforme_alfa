import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ExerciceService } from '../../core/services/exercice.service';
import { CoursService } from '../../core/services/cours.service';
import { Exercice, Matiere } from '../../core/models/models';

@Component({
  selector: 'app-exercices',
  standalone: false,
  templateUrl: './exercices.html',
  styleUrl: './exercices.scss',
})
export class Exercices implements OnInit {
  allExercices: Exercice[] = [];
  filteredExercices: Exercice[] = [];
  matieres: Matiere[] = [];
  isLoading = true;
  errorMessage = '';
  selectedType: string | null = null;
  selectedMatiereId: number | null = null;

  types = [
    { value: null, label: 'Tous' },
    { value: 'QCM', label: 'QCM' },
    { value: 'EXERCICE', label: 'Exercices' },
    { value: 'ANNALE', label: 'Annales' },
  ];

  constructor(
    private exerciceService: ExerciceService,
    private coursService: CoursService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadExercices();
  }

  loadExercices(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.coursService.getMatieres().subscribe({
      next: (matieres) => (this.matieres = matieres),
    });

    this.exerciceService.getAllExercices().subscribe({
      next: (exercices) => {
        this.allExercices = exercices;
        this.filteredExercices = exercices;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger les exercices. Vérifiez que le serveur est démarré.';
        this.isLoading = false;
      },
    });
  }

  applyFilters(): void {
    this.filteredExercices = this.allExercices.filter((e) => {
      const typeMatch = !this.selectedType || e.type === this.selectedType;
      const matiereMatch =
        !this.selectedMatiereId || e.matiere.id === this.selectedMatiereId;
      return typeMatch && matiereMatch;
    });
  }

  filterByType(type: string | null): void {
    this.selectedType = type;
    this.applyFilters();
  }

  filterByMatiere(matiereId: number | null): void {
    this.selectedMatiereId = matiereId;
    this.applyFilters();
  }

  getDifficulteColor(diff: string): string {
    switch (diff) {
      case 'FACILE':
        return '#2e7d32';
      case 'MOYEN':
        return '#e65100';
      case 'DIFFICILE':
        return '#c62828';
      default:
        return '#1a237e';
    }
  }

  getDifficulteLabel(diff: string): string {
    switch (diff) {
      case 'FACILE':
        return 'Facile';
      case 'MOYEN':
        return 'Moyen';
      case 'DIFFICILE':
        return 'Difficile';
      default:
        return diff;
    }
  }

  getTypeIcon(type: string): string {
    switch (type) {
      case 'QCM':
        return 'quiz';
      case 'EXERCICE':
        return 'assignment';
      case 'ANNALE':
        return 'history_edu';
      default:
        return 'assignment';
    }
  }

  getCountForType(type: string | null): number {
    if (!type) return this.allExercices.length;
    return this.allExercices.filter((e) => e.type === type).length;
  }

  openExercice(id: number): void {
    this.router.navigate(['/exercices', id]);
  }
}
