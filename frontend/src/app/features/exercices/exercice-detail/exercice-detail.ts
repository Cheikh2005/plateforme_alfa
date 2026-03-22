import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ExerciceService } from '../../../core/services/exercice.service';
import { Exercice, SubmitResult } from '../../../core/models/models';
import { timeout } from 'rxjs';

@Component({
  selector: 'app-exercice-detail',
  standalone: false,
  templateUrl: './exercice-detail.html',
  styleUrl: './exercice-detail.scss',
})
export class ExerciceDetail implements OnInit, OnDestroy {
  exercice: Exercice | null = null;
  isLoading = true;
  isSubmitting = false;
  errorMessage = '';

  // QCM answers: { questionId: optionId }
  selectedAnswers: { [questionId: number]: number } = {};

  // Result
  showResult = false;
  result: SubmitResult | null = null;

  // EXERCICE/ANNALE correction
  showCorrection = false;

  // Timer
  elapsedSeconds = 0;
  private timerInterval: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private exerciceService: ExerciceService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id && id > 0) {
      this.loadExercice(id);
    } else {
      this.errorMessage = 'Exercice introuvable.';
      this.isLoading = false;
    }
  }

  ngOnDestroy(): void {
    this.stopTimer();
  }

  loadExercice(id: number): void {
    this.exerciceService.getById(id).pipe(timeout(15000)).subscribe({
      next: (ex) => {
        this.exercice = ex;
        this.isLoading = false;
        this.startTimer();
      },
      error: () => {
        this.errorMessage = 'Impossible de charger cet exercice. Vérifiez que le serveur est démarré.';
        this.isLoading = false;
      },
    });
  }

  startTimer(): void {
    this.timerInterval = setInterval(() => {
      this.elapsedSeconds++;
    }, 1000);
  }

  stopTimer(): void {
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
    }
  }

  get elapsedMinutes(): number {
    return Math.floor(this.elapsedSeconds / 60);
  }

  get elapsedSecondsDisplay(): string {
    const s = this.elapsedSeconds % 60;
    return s < 10 ? `0${s}` : `${s}`;
  }

  get timerDisplay(): string {
    return `${this.elapsedMinutes}:${this.elapsedSecondsDisplay}`;
  }

  selectAnswer(questionId: number, optionId: number): void {
    this.selectedAnswers[questionId] = optionId;
  }

  get allQuestionsAnswered(): boolean {
    if (!this.exercice?.questions) return false;
    return this.exercice.questions.every(
      (q) => this.selectedAnswers[q.id] !== undefined
    );
  }

  submitAnswers(): void {
    if (!this.exercice) return;
    this.isSubmitting = true;
    this.stopTimer();
    const tempsMinutes = Math.ceil(this.elapsedSeconds / 60);
    this.exerciceService
      .submitAnswers(this.exercice.id, this.selectedAnswers, tempsMinutes)
      .subscribe({
        next: (result) => {
          this.result = result;
          this.showResult = true;
          this.isSubmitting = false;
        },
        error: () => {
          this.isSubmitting = false;
        },
      });
  }

  toggleCorrection(): void {
    this.showCorrection = !this.showCorrection;
    if (this.showCorrection) {
      this.stopTimer();
    }
  }

  isCorrectAnswer(questionId: number, optionId: number): boolean {
    if (!this.result) return false;
    return this.result.resultats[questionId] === true &&
      this.selectedAnswers[questionId] === optionId;
  }

  isWrongAnswer(questionId: number, optionId: number): boolean {
    if (!this.result) return false;
    return this.result.resultats[questionId] === false &&
      this.selectedAnswers[questionId] === optionId;
  }

  isCorrectOptionInResult(questionId: number, option: any): boolean {
    return !!option.estCorrecte;
  }

  getMentionClass(): string {
    if (!this.result) return '';
    const m = this.result.mention.toLowerCase();
    if (m.includes('très bien') || m.includes('excellent')) return 'excellent';
    if (m.includes('bien')) return 'bien';
    if (m.includes('assez')) return 'assez-bien';
    return 'insuffisant';
  }

  getDifficulteColor(diff: string): string {
    switch (diff) {
      case 'FACILE': return '#2e7d32';
      case 'MOYEN': return '#e65100';
      case 'DIFFICILE': return '#c62828';
      default: return '#1a237e';
    }
  }

  objectKeys(obj: any): string[] {
    return Object.keys(obj);
  }

  goBack(): void {
    this.router.navigate(['/exercices']);
  }
}
