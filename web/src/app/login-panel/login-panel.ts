import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { finalize } from 'rxjs';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-login-panel',
  imports: [FormsModule],
  templateUrl: './login-panel.html',
  styleUrl: './login-panel.css',
})
export class LoginPanel implements OnInit {
  readonly auth = inject(AuthService);
  credentials = { username: '', email: '', password: '' };
  registering = false;
  submitting = false;
  errorMessage = '';

  ngOnInit() { this.auth.restore(); }

  submit() {
    if (this.submitting) return;
    this.submitting = true;
    this.errorMessage = '';
    const request = this.registering
      ? this.auth.register(this.credentials)
      : this.auth.login({ username: this.credentials.username, password: this.credentials.password });
    request.pipe(finalize(() => this.submitting = false)).subscribe({
      next: () => this.credentials.password = '',
      error: (error: HttpErrorResponse) => {
        const validation = error.error?.validationErrors;
        this.errorMessage = validation ? Object.values(validation).join(' ') :
          error.status === 401 ? 'Nieprawidłowy login lub hasło.' :
          error.status === 409 ? 'Login lub adres e-mail jest już zajęty.' :
          error.status === 0 ? 'Nie można połączyć się z serwerem.' :
          'Nie udało się wykonać operacji. Sprawdź dane i spróbuj ponownie.';
      },
    });
  }

  switchMode() {
    this.registering = !this.registering;
    this.errorMessage = '';
    this.credentials.password = '';
  }
}
