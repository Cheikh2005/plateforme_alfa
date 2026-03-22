import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Planning } from '../models/models';

@Injectable({ providedIn: 'root' })
export class PlanningService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Planning[]> {
    return this.http.get<Planning[]>(`${this.apiUrl}/plannings`);
  }

  create(planning: Partial<Planning>): Observable<Planning> {
    return this.http.post<Planning>(`${this.apiUrl}/plannings`, planning);
  }

  update(id: number, planning: Partial<Planning>): Observable<Planning> {
    return this.http.put<Planning>(`${this.apiUrl}/plannings/${id}`, planning);
  }

  toggleComplete(id: number): Observable<Planning> {
    return this.http.patch<Planning>(`${this.apiUrl}/plannings/${id}/toggle`, {});
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/plannings/${id}`);
  }
}
