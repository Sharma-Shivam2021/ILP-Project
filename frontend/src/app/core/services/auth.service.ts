import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { LoginRequestDto, LoginResponseDto, User, RegisterRequestDto, DepartmentResponse } from '../models/models';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = '/api/auth';
  private currentUserSubject = new BehaviorSubject<User | null>(this.getUserFromStorage());
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) { }

  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  public get isFirstLogin(): boolean {
    return localStorage.getItem('firstLogin') === 'true';
  }

  login(credentials: LoginRequestDto): Observable<LoginResponseDto> {
    // Clear any stale token BEFORE making the login request
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('firstLogin');
    this.currentUserSubject.next(null);

    return this.http.post<LoginResponseDto>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        if (response && response.token) {
          const userObj: User = {
            id: response.userId,
            email: response.username,
            name: response.fullName || response.username,
            role: response.role
          };
          localStorage.setItem('token', response.token);
          localStorage.setItem('user', JSON.stringify(userObj));

          // Store firstLogin flag separately
          if (response.firstLogin) {
            localStorage.setItem('firstLogin', 'true');
          }

          this.currentUserSubject.next(userObj);
        }
      })
    );
  }

  register(userData: RegisterRequestDto): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/register`, userData);
  }

  getDepartments(): Observable<DepartmentResponse[]> {
    return this.http.get<DepartmentResponse[]>('/api/departments');
  }

  clearFirstLogin() {
    localStorage.removeItem('firstLogin');
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('firstLogin');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  private getUserFromStorage(): User | null {
    const token = localStorage.getItem('token');
    const userStr = localStorage.getItem('user');

    if (!token || !userStr) {
      return null;
    }

    // Check if the JWT token is expired before trusting the stored session
    if (this.isTokenExpired(token)) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('firstLogin');
      return null;
    }

    try {
      return JSON.parse(userStr);
    } catch (e) {
      return null;
    }
  }

  /** Decodes the JWT payload and checks if the `exp` claim has passed. */
  isTokenExpired(token: string): boolean {
    try {
      const payloadBase64 = token.split('.')[1];
      const payload = JSON.parse(atob(payloadBase64));
      // `exp` is in seconds; Date.now() is in milliseconds
      return payload.exp * 1000 < Date.now();
    } catch (e) {
      // If we can't parse the token, treat it as expired
      return true;
    }
  }
}
