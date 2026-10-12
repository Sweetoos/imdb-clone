import { TestBed } from '@angular/core/testing';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { LoginPanel } from './login-panel';
import { AuthService } from '../auth/auth.service';
import { authInterceptor } from '../auth/auth.interceptor';

describe('Authentication', () => {
  let http: HttpTestingController;
  let auth: AuthService;
  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      imports: [LoginPanel],
      providers: [provideHttpClient(withInterceptors([authInterceptor])), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    auth = TestBed.inject(AuthService);
  });
  afterEach(() => { http.verify(); localStorage.clear(); });

  it('logs in and clears the password without displaying the token', () => {
    const fixture = TestBed.createComponent(LoginPanel);
    const component = fixture.componentInstance;
    component.credentials = { username: 'alice', email: '', password: 'password123' };
    component.submit();
    const request = http.expectOne('http://localhost:8080/auth/login');
    expect(request.request.headers.has('Authorization')).toBe(false);
    expect(request.request.body).toEqual({ username: 'alice', password: 'password123' });
    request.flush({ token: 'secret-token', user: { userId: 1, username: 'alice', email: 'alice@example.com' } });
    expect(auth.user()?.username).toBe('alice');
    expect(localStorage.getItem('jwt_token')).toBe('secret-token');
    expect(component.credentials.password).toBe('');
    fixture.detectChanges();
    // ngOnInit verifies the persisted session.
    http.expectOne('http://localhost:8080/auth/me').flush(auth.user());
    expect(fixture.nativeElement.textContent).not.toContain('secret-token');
  });

  it('registers and displays duplicate-account errors', () => {
    const component = TestBed.createComponent(LoginPanel).componentInstance;
    component.registering = true;
    component.credentials = { username: 'alice', email: 'alice@example.com', password: 'password123' };
    component.submit();
    http.expectOne('http://localhost:8080/auth/register').flush({}, { status: 409, statusText: 'Conflict' });
    expect(component.errorMessage).toContain('zajęty');
    expect(component.submitting).toBe(false);
  });

  it('restores the session using a bearer token and clears it after a 401', () => {
    auth.token.set('old-token');
    localStorage.setItem('jwt_token', 'old-token');
    auth.restore();
    const request = http.expectOne('http://localhost:8080/auth/me');
    expect(request.request.headers.get('Authorization')).toBe('Bearer old-token');
    request.flush({}, { status: 401, statusText: 'Unauthorized' });
    expect(auth.token()).toBeNull();
    expect(localStorage.getItem('jwt_token')).toBeNull();
  });
});
