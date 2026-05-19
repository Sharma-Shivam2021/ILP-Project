import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { LoginRequestDto, LoginResponseDto, User } from '../models/models';
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
            name: response.username,
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
    const userStr = localStorage.getItem('user');
    if (userStr) {
      try {
        return JSON.parse(userStr);
      } catch (e) {
        return null;
      }
    }
    return null;
  }
}
