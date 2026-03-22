import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ChatMessage } from '../models/models';

@Injectable({ providedIn: 'root' })
export class ChatService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  sendMessage(message: string, matiereId?: number): Observable<{ reponse: string }> {
    return this.http.post<{ reponse: string }>(`${this.apiUrl}/chat/send`, { message, matiereId });
  }

  getHistory(): Observable<ChatMessage[]> {
    return this.http.get<ChatMessage[]>(`${this.apiUrl}/chat/history`);
  }

  clearHistory(): Observable<any> {
    return this.http.delete(`${this.apiUrl}/chat/history`);
  }
}
