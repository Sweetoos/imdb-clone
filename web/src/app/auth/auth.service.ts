import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';

export const API_URL = 'http://localhost:8080';
export interface CurrentUser {
  userId: number;
  username: string;
  email: string;
  createdAt: string;
}
interface AuthResponse { token: string; user: CurrentUser }

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  readonly user = signal<CurrentUser | null>(null);
  readonly token = signal<string | null>(localStorage.getItem('jwt_token'));

  login(credentials: { username: string; password: string }) {
    return this.http.post<AuthResponse>(`${API_URL}/auth/login`, credentials).pipe(
      tap(response => this.save(response)),
    );
  }

  register(credentials: { username: string; email: string; password: string }) {
    return this.http.post<AuthResponse>(`${API_URL}/auth/register`, credentials).pipe(
      tap(response => this.save(response)),
    );
  }

  restore() {
    if (this.token()) {
      this.http.get<CurrentUser>(`${API_URL}/auth/me`).subscribe({
        next: user => this.user.set(user),
        error: () => this.logout(),
      });
    }
  }

  logout() {
    localStorage.removeItem('jwt_token');
    this.token.set(null);
    this.user.set(null);
  }

  private save(response: AuthResponse) {
    localStorage.setItem('jwt_token', response.token);
    this.token.set(response.token);
    this.user.set(response.user);
  }
}
