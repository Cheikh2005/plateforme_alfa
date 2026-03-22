import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { Cours, Matiere, UserProgress } from '../models/models';

@Injectable({ providedIn: 'root' })
export class CoursService {
  private apiUrl = environment.apiUrl;

  private matieresCache: Matiere[] | null = null;
  private coursCache: Cours[] | null = null;

  constructor(private http: HttpClient) {}

  getMatieres(): Observable<Matiere[]> {
    if (this.matieresCache) {
      return of(this.matieresCache);
    }
    return this.http.get<Matiere[]>(`${this.apiUrl}/matieres`).pipe(
      tap(data => this.matieresCache = data)
    );
  }

  getAllCours(): Observable<Cours[]> {
    if (this.coursCache) {
      return of(this.coursCache);
    }
    return this.http.get<Cours[]>(`${this.apiUrl}/cours`).pipe(
      tap(data => this.coursCache = data)
    );
  }

  getCoursByMatiere(matiereId: number): Observable<Cours[]> {
    return this.http.get<Cours[]>(`${this.apiUrl}/cours/matiere/${matiereId}`);
  }

  getCoursById(id: number): Observable<Cours> {
    return this.http.get<Cours>(`${this.apiUrl}/cours/${id}`);
  }

  markProgress(coursId: number, pourcentage: number = 100): Observable<UserProgress> {
    this.coursCache = null; // invalidate cache on progress update
    return this.http.post<UserProgress>(`${this.apiUrl}/cours/${coursId}/progress`, { pourcentage });
  }

  getUserProgress(): Observable<UserProgress[]> {
    return this.http.get<UserProgress[]>(`${this.apiUrl}/progress`);
  }

  clearCache(): void {
    this.matieresCache = null;
    this.coursCache = null;
  }
}
