import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { API_URL, AuthService } from './auth.service';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const auth = inject(AuthService);
  const url = new URL(request.url, window.location.origin);
  const isApi = url.origin === API_URL;
  const isAuthentication = url.pathname === '/auth/login' || url.pathname === '/auth/register';
  const token = auth.token();
  const authenticatedRequest = isApi && !isAuthentication && token
    ? request.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : request;
  return next(authenticatedRequest).pipe(catchError((error: HttpErrorResponse) => {
    // A delayed response from an old session must not clear a newly issued token.
    if (isApi && !isAuthentication && error.status === 401 && auth.token() === token) {
      auth.logout();
    }
    return throwError(() => error);
  }));
};
