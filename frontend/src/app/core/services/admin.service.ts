import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private apiUrl = environment.apiUrl;
  constructor(private http: HttpClient) {}

  getUsers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/admin/users`);
  }
  createUser(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/admin/users`, data);
  }
  updateUser(id: number, data: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/admin/users/${id}`, data);
  }
  deleteUser(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/admin/users/${id}`);
  }
}