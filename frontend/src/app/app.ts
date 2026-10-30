import { HttpClient } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { timeout } from 'rxjs';

@Component({
  selector: 'app-root',
  imports: [FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  private readonly http = inject(HttpClient);

  customerReference = '';
  openingBalance = 500;
  accountId = signal('');
  message = '';
  answer = signal('');
  error = signal('');
  creatingAccount = signal(false);
  sendingMessage = signal(false);

  createAccount(): void {
    this.error.set('');
    this.creatingAccount.set(true);
    this.http.post<{ id: string }>('/accounts', {
      customerReference: this.customerReference,
      currency: 'EUR',
      openingBalance: this.openingBalance,
    }).pipe(timeout(15000)).subscribe({
      next: (account) => {
        this.accountId.set(account.id);
        this.message = `What is the balance of account ${account.id}? Use the banking tool.`;
        this.creatingAccount.set(false);
      },
      error: () => {
        this.error.set('Could not create the account. Check that account-service is running on port 8091.');
        this.creatingAccount.set(false);
      },
    });
  }

  ask(): void {
    if (!this.message.trim()) return;
    this.error.set('');
    this.answer.set('');
    this.sendingMessage.set(true);
    this.http.post<{ answer: string }>('/chat', { message: this.message }).pipe(timeout(60000)).subscribe({
      next: (response) => {
        this.answer.set(response.answer);
        this.sendingMessage.set(false);
      },
      error: () => {
        this.error.set('Chat failed. Check that chatbot-service is running on port 8093.');
        this.sendingMessage.set(false);
      },
    });
  }
}
