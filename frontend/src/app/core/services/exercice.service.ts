import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { Exercice, UserScore, SubmitResult } from '../models/models';

@Injectable({ providedIn: 'root' })
export class ExerciceService {
  private apiUrl = environment.apiUrl;

  private exercicesCache: Exercice[] | null = null;

  constructor(private http: HttpClient) {}

  getAllExercices(): Observable<Exercice[]> {
    if (this.exercicesCache) {
      return of(this.exercicesCache);
    }
    return this.http.get<Exercice[]>(`${this.apiUrl}/exercices`).pipe(
      tap(data => this.exercicesCache = data)
    );
  }

  getByMatiere(matiereId: number): Observable<Exercice[]> {
    return this.http.get<Exercice[]>(`${this.apiUrl}/exercices/matiere/${matiereId}`);
  }

  getByType(type: string): Observable<Exercice[]> {
    return this.http.get<Exercice[]>(`${this.apiUrl}/exercices/type/${type}`);
  }

  getById(id: number): Observable<Exercice> {
    return this.http.get<Exercice>(`${this.apiUrl}/exercices/${id}`);
  }

  submitAnswers(exerciceId: number, reponses: { [key: number]: number }, temps?: number): Observable<SubmitResult> {
    this.exercicesCache = null; // invalidate cache after submission
    return this.http.post<SubmitResult>(`${this.apiUrl}/exercices/submit`, {
      exerciceId, reponses, tempsPasseMinutes: temps
    });
  }

  getScores(): Observable<UserScore[]> {
    return this.http.get<UserScore[]>(`${this.apiUrl}/exercices/scores`);
  }

  clearCache(): void {
    this.exercicesCache = null;
  }
}
