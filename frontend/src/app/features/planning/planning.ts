import { Component, OnInit } from '@angular/core';
import { PlanningService } from '../../core/services/planning.service';
import { CoursService } from '../../core/services/cours.service';
import { Planning as PlanningModel, Matiere } from '../../core/models/models';

@Component({
  selector: 'app-planning',
  standalone: false,
  templateUrl: './planning.html',
  styleUrl: './planning.scss',
})
export class Planning implements OnInit {
  plannings: PlanningModel[] = [];
  matieres: Matiere[] = [];
  isLoading = true;
  showForm = false;
  filterBy: 'all' | 'pending' | 'done' = 'all';

  presetColors = [
    '#1a237e', '#c62828', '#1b5e20', '#e65100',
    '#4a148c', '#006064', '#f57f17', '#37474f',
  ];

  newPlanning = {
    titre: '',
    description: '',
    dateDebut: '',
    dateFin: '',
    heureDebut: '',
    heureFin: '',
    matiereId: null as number | null,
    couleur: '#1a237e',
    rappel: false,
  };

  constructor(
    private planningService: PlanningService,
    private coursService: CoursService
  ) {}

  ngOnInit(): void {
    this.coursService.getMatieres().subscribe({
      next: (matieres) => (this.matieres = matieres),
    });
    this.loadPlannings();
  }

  loadPlannings(): void {
    this.isLoading = true;
    this.planningService.getAll().subscribe({
      next: (data) => {
        this.plannings = data;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  get filteredPlannings(): PlanningModel[] {
    switch (this.filterBy) {
      case 'pending':
        return this.plannings.filter((p) => !p.complete);
      case 'done':
        return this.plannings.filter((p) => p.complete);
      default:
        return this.plannings;
    }
  }

  get pendingPlannings(): PlanningModel[] {
    return this.plannings.filter((p) => !p.complete);
  }

  get donePlannings(): PlanningModel[] {
    return this.plannings.filter((p) => p.complete);
  }

  toggleForm(): void {
    this.showForm = !this.showForm;
    if (!this.showForm) {
      this.resetForm();
    }
  }

  createPlanning(): void {
    if (!this.newPlanning.titre) return;

    const payload: any = {
      titre: this.newPlanning.titre,
      description: this.newPlanning.description,
      dateDebut: this.newPlanning.dateDebut,
      dateFin: this.newPlanning.dateFin,
      heureDebut: this.newPlanning.heureDebut,
      heureFin: this.newPlanning.heureFin,
      couleur: this.newPlanning.couleur,
      rappel: this.newPlanning.rappel,
      complete: false,
    };

    if (this.newPlanning.matiereId) {
      payload.matiere = { id: this.newPlanning.matiereId };
    }

    this.planningService.create(payload).subscribe({
      next: (p) => {
        this.plannings.unshift(p);
        this.showForm = false;
        this.resetForm();
      },
    });
  }

  toggleComplete(id: number): void {
    this.planningService.toggleComplete(id).subscribe({
      next: (updated) => {
        const idx = this.plannings.findIndex((p) => p.id === id);
        if (idx !== -1) {
          this.plannings[idx] = updated;
        }
      },
    });
  }

  deletePlanning(id: number): void {
    this.planningService.delete(id).subscribe({
      next: () => {
        this.plannings = this.plannings.filter((p) => p.id !== id);
      },
    });
  }

  getMatiereName(id: number | null | undefined): string {
    if (!id) return '';
    const m = this.matieres.find((mat) => mat.id === id);
    return m ? m.nom : '';
  }

  getMatiereColor(planning: PlanningModel): string {
    return planning.couleur || planning.matiere?.couleur || '#1a237e';
  }

  selectColor(color: string): void {
    this.newPlanning.couleur = color;
  }

  resetForm(): void {
    this.newPlanning = {
      titre: '',
      description: '',
      dateDebut: '',
      dateFin: '',
      heureDebut: '',
      heureFin: '',
      matiereId: null,
      couleur: '#1a237e',
      rappel: false,
    };
  }

  formatDate(dateStr: string): string {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    return d.toLocaleDateString('fr-FR', { day: '2-digit', month: 'short', year: 'numeric' });
  }
}
