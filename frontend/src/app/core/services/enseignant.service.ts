import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class EnseignantService {
  private apiUrl = environment.apiUrl;
  constructor(private http: HttpClient) {}

  // Cours
  createCours(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/enseignant/cours`, data);
  }
  updateCours(id: number, data: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/enseignant/cours/${id}`, data);
  }
  deleteCours(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/enseignant/cours/${id}`);
  }

  // Exercices
  createExercice(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/enseignant/exercices`, data);
  }
  updateExercice(id: number, data: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/enseignant/exercices/${id}`, data);
  }
  deleteExercice(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/enseignant/exercices/${id}`);
  }

  // Questions
  addQuestion(exerciceId: number, data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/enseignant/exercices/${exerciceId}/questions`, data);
  }
  deleteQuestion(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/enseignant/questions/${id}`);
  }
}