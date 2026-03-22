import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { CoursService } from '../../../core/services/cours.service';
import { Cours } from '../../../core/models/models';
import { timeout } from 'rxjs';

@Component({
  selector: 'app-cours-detail',
  standalone: false,
  templateUrl: './cours-detail.html',
  styleUrl: './cours-detail.scss',
})
export class CoursDetail implements OnInit {
  cours: Cours | null = null;
  isLoading = true;
  isMarkingComplete = false;
  isComplete = false;
  errorMessage = '';
  formattedContent: SafeHtml = '';
  formattedResume: SafeHtml = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private coursService: CoursService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id && id > 0) {
      this.loadCours(id);
      this.loadProgress(id);
    } else {
      this.errorMessage = 'Cours introuvable.';
      this.isLoading = false;
    }
  }

  loadCours(id: number): void {
    this.coursService.getCoursById(id).pipe(timeout(15000)).subscribe({
      next: (cours) => {
        this.cours = cours;
        this.formattedContent = this.sanitizer.bypassSecurityTrustHtml(
          this.formatContent(cours.contenu)
        );
        this.formattedResume = this.sanitizer.bypassSecurityTrustHtml(
          this.formatContent(cours.resume)
        );
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger ce cours. Vérifiez que le serveur est démarré.';
        this.isLoading = false;
      },
    });
  }

  loadProgress(id: number): void {
    this.coursService.getUserProgress().subscribe({
      next: (progress) => {
        const p = progress.find((up) => up.cours.id === id);
        this.isComplete = p ? p.complete : false;
      },
    });
  }

  formatContent(content: string): string {
    if (!content) return '';
    return content
      .replace(/^### (.+)$/gm, '<h3>$1</h3>')
      .replace(/^## (.+)$/gm, '<h2>$1</h2>')
      .replace(/^# (.+)$/gm, '<h1>$1</h1>')
      .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
      .replace(/\*(.+?)\*/g, '<em>$1</em>')
      .replace(/`(.+?)`/g, '<code>$1</code>')
      .replace(/^- (.+)$/gm, '<li>$1</li>')
      .replace(/(<li>.*<\/li>)/gs, '<ul>$1</ul>')
      .replace(/\n\n/g, '</p><p>')
      .replace(/^(?!<[hul])(.+)$/gm, (m) => (m.trim() ? `<p>${m}</p>` : ''));
  }

  markAsComplete(): void {
    if (!this.cours) return;
    this.isMarkingComplete = true;
    this.coursService.markProgress(this.cours.id, 100).subscribe({
      next: () => {
        this.isComplete = true;
        this.isMarkingComplete = false;
      },
      error: () => {
        this.isMarkingComplete = false;
      },
    });
  }

  goBack(): void {
    this.router.navigate(['/cours']);
  }
}
