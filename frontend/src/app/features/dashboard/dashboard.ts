import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DashboardService } from '../../core/services/dashboard.service';
import { DashboardStats } from '../../core/models/models';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit {
  stats: DashboardStats | null = null;
  isLoading = true;
  errorMessage = '';

  displayedColumns = ['exercice', 'score', 'mention', 'date'];

  constructor(
    private dashboardService: DashboardService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadStats();
  }

  loadStats(): void {
    this.isLoading = true;
    this.dashboardService.getStats().subscribe({
      next: (data) => {
        this.stats = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger les statistiques.';
        this.isLoading = false;
      },
    });
  }

  getCoursProgressPercent(): number {
    if (!this.stats || this.stats.totalCours === 0) return 0;
    return Math.round((this.stats.coursComplets / this.stats.totalCours) * 100);
  }

  getTasksProgressPercent(): number {
    if (!this.stats || this.stats.totalTasks === 0) return 0;
    return Math.round((this.stats.tasksDone / this.stats.totalTasks) * 100);
  }

  getExercicesProgressPercent(): number {
    if (!this.stats || this.stats.totalExercices === 0) return 0;
    return Math.round((this.stats.exercicesFaits / this.stats.totalExercices) * 100);
  }

  getMentionColor(mention: string): string {
    if (!mention) return 'default';
    const m = mention.toLowerCase();
    if (m.includes('très bien') || m.includes('excellent')) return 'accent';
    if (m.includes('bien')) return 'primary';
    if (m.includes('assez bien')) return 'warn-light';
    return 'default';
  }

  navigateTo(path: string): void {
    this.router.navigate([path]);
  }

  formatDate(dateStr: string): string {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    return d.toLocaleDateString('fr-FR', { day: '2-digit', month: 'short', year: 'numeric' });
  }
}
