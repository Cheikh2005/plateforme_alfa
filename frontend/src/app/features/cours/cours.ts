import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CoursService } from '../../core/services/cours.service';
import { Cours as CoursModel, Matiere, UserProgress } from '../../core/models/models';

@Component({
  selector: 'app-cours',
  standalone: false,
  templateUrl: './cours.html',
  styleUrl: './cours.scss',
})
export class Cours implements OnInit {
  allCours: CoursModel[] = [];
  filteredCours: CoursModel[] = [];
  matieres: Matiere[] = [];
  userProgress: UserProgress[] = [];
  isLoading = true;
  errorMessage = '';
  selectedMatiereId: number | null = null;
  searchText = '';

  constructor(
    private coursService: CoursService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.coursService.getMatieres().subscribe({
      next: (matieres) => {
        this.matieres = matieres;
      },
    });

    this.coursService.getUserProgress().subscribe({
      next: (progress) => {
        this.userProgress = progress;
      },
    });

    this.coursService.getAllCours().subscribe({
      next: (cours) => {
        this.allCours = cours;
        this.filteredCours = cours;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger les cours. Vérifiez que le serveur est démarré.';
        this.isLoading = false;
      },
    });
  }

  filterByMatiere(matiereId: number | null): void {
    this.selectedMatiereId = matiereId;
    this.applyFilters();
  }

  onSearch(): void {
    this.applyFilters();
  }

  applyFilters(): void {
    let result = this.allCours;
    if (this.selectedMatiereId !== null) {
      result = result.filter((c) => c.matiere.id === this.selectedMatiereId);
    }
    if (this.searchText.trim()) {
      const s = this.searchText.toLowerCase();
      result = result.filter(
        (c) =>
          c.titre.toLowerCase().includes(s) ||
          c.chapitre.toLowerCase().includes(s) ||
          c.matiere.nom.toLowerCase().includes(s)
      );
    }
    this.filteredCours = result;
  }

  getProgressForCours(coursId: number): number {
    const p = this.userProgress.find((up) => up.cours.id === coursId);
    return p ? p.pourcentage : 0;
  }

  isComplete(coursId: number): boolean {
    const p = this.userProgress.find((up) => up.cours.id === coursId);
    return p ? p.complete : false;
  }

  openCours(id: number): void {
    this.router.navigate(['/cours', id]);
  }

  getMatiereColor(matiere: Matiere): string {
    return matiere?.couleur || '#1a237e';
  }
}
