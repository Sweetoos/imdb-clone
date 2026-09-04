import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login-panel',
  imports: [FormsModule],
  templateUrl: './login-panel.html',
  styleUrl: './login-panel.css',
})
export class LoginPanel {
  private http = inject(HttpClient);
  credentials = { username: '', password: '' };
  token: string | null = null;
  errorMessage = '';

  login() {
    this.http.post<any>('http://localhost:8080/auth/login', this.credentials).subscribe({
      next: (response) => {
        this.token = response.token;
        localStorage.setItem('jwt_token', response.token);
        this.errorMessage = '';
        console.log('Login success', response);
      },
      error: (err) => {
        this.token = null;
        this.errorMessage = 'Login error';
        console.error('connection fail with backend', err);
      },
    });
  }
  logout() {
    this.token = null;
    localStorage.removeItem('jwt_token');
  }
}
