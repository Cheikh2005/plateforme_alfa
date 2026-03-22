import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { DashboardStats } from '../models/models';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private apiUrl = environment.apiUrl;
  private statsCache: DashboardStats | null = null;
  private cacheTimestamp = 0;
  private readonly CACHE_TTL_MS = 30_000; // 30 secondes

  constructor(private http: HttpClient) {}

  getStats(): Observable<DashboardStats> {
    const now = Date.now();
    if (this.statsCache && now - this.cacheTimestamp < this.CACHE_TTL_MS) {
      return of(this.statsCache);
    }
    return this.http.get<DashboardStats>(`${this.apiUrl}/dashboard/stats`).pipe(
      tap(data => {
        this.statsCache = data;
        this.cacheTimestamp = Date.now();
      })
    );
  }

  invalidateCache(): void {
    this.statsCache = null;
  }
}
