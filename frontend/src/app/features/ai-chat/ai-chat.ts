import { Component, OnInit, AfterViewChecked, ViewChild, ElementRef } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ChatService } from '../../core/services/chat.service';
import { CoursService } from '../../core/services/cours.service';
import { Matiere } from '../../core/models/models';

interface ChatMessage {
  role: 'user' | 'assistant';
  content: string;
  formattedContent?: SafeHtml;
  time: Date;
}

@Component({
  selector: 'app-ai-chat',
  standalone: false,
  templateUrl: './ai-chat.html',
  styleUrl: './ai-chat.scss',
})
export class AiChat implements OnInit, AfterViewChecked {
  @ViewChild('messagesEnd') messagesEnd!: ElementRef;

  messages: ChatMessage[] = [];
  matieres: Matiere[] = [];
  userMessage = '';
  isLoading = false;
  selectedMatiereId: number | undefined = undefined;

  quickSuggestions = [
    'Explique-moi les dérivées',
    'Aide-moi avec la génétique',
    'Qu\'est-ce que la photosynthèse ?',
    'Comment résoudre les équations du second degré ?',
    'Explique les lois de Newton',
  ];

  constructor(
    private chatService: ChatService,
    private coursService: CoursService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.coursService.getMatieres().subscribe({
      next: (matieres) => (this.matieres = matieres),
    });

    this.chatService.getHistory().subscribe({
      next: (history) => {
        this.messages = [];
        history.forEach((msg) => {
          this.messages.push({
            role: 'user',
            content: msg.message,
            time: new Date(msg.dateMessage),
          });
          if (msg.reponse) {
            this.messages.push({
              role: 'assistant',
              content: msg.reponse,
              formattedContent: this.formatMarkdown(msg.reponse),
              time: new Date(msg.dateMessage),
            });
          }
        });
      },
      error: () => {
        // History unavailable, start fresh
      },
    });
  }

  formatMarkdown(text: string): SafeHtml {
    if (!text) return this.sanitizer.bypassSecurityTrustHtml('');
    let html = text
      // Bold
      .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
      // Italic
      .replace(/\*(.+?)\*/g, '<em>$1</em>')
      // Inline code
      .replace(/`([^`]+)`/g, '<code style="background:#f0f0f0;padding:1px 5px;border-radius:4px;font-size:0.9em;">$1</code>')
      // Line breaks
      .replace(/\n/g, '<br>');
    return this.sanitizer.bypassSecurityTrustHtml(html);
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  scrollToBottom(): void {
    try {
      if (this.messagesEnd) {
        this.messagesEnd.nativeElement.scrollIntoView({ behavior: 'smooth' });
      }
    } catch (e) {}
  }

  sendMessage(text?: string): void {
    const msg = text || this.userMessage.trim();
    if (!msg || this.isLoading) return;

    this.messages.push({ role: 'user', content: msg, time: new Date() });
    this.userMessage = '';
    this.isLoading = true;

    this.chatService.sendMessage(msg, this.selectedMatiereId).subscribe({
      next: (resp) => {
        this.messages.push({
          role: 'assistant',
          content: resp.reponse,
          formattedContent: this.formatMarkdown(resp.reponse),
          time: new Date(),
        });
        this.isLoading = false;
      },
      error: () => {
        const errMsg = "Désolé, une erreur s'est produite. Veuillez réessayer.";
        this.messages.push({
          role: 'assistant',
          content: errMsg,
          formattedContent: this.formatMarkdown(errMsg),
          time: new Date(),
        });
        this.isLoading = false;
      },
    });
  }

  clearChat(): void {
    this.chatService.clearHistory().subscribe({
      next: () => {
        this.messages = [];
      },
    });
  }

  onKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  formatTime(date: Date): string {
    return date.toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' });
  }
}
