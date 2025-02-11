import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private BASE_URL = 'http://localhost:8080/auth';

  private authState = new BehaviorSubject<boolean>(this.hasToken());
  isAuthenticated$ = this.authState.asObservable();

  constructor(private http: HttpClient, private router: Router) {}

  // Check if token exists
  private hasToken(): boolean {
    return !!localStorage.getItem('token');
  }

  // Login Method
  login(credentials: { email: string; password: string }): Observable<any> {
    return new Observable((observer) => {
      this.http.post(`${this.BASE_URL}/login`, credentials).subscribe({
        next: (response: any) => {
          localStorage.setItem('token', response.token);
          this.authState.next(true);
          observer.next(response);
          observer.complete();
        },
        error: (err) => observer.error(err),
      });
    });
  }

  // Register Method
  register(user: any): Observable<string> {
    return this.http.post(`${this.BASE_URL}/register`, user, {
      responseType: 'text', // ✅ Ensure response is treated as a string
    });
  }

  // Logout Method
  logout(): void {
    localStorage.removeItem('token');
    this.authState.next(false);
    this.router.navigate(['/login']);
  }

  // Check User Role
  getUserRole(): string | null {
    const token = localStorage.getItem('token');
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.role;
    }
    return null;
  }

  // Check if User is Admin
  isAdmin(): boolean {
    return this.getUserRole() === 'ADMIN';
  }

  // Forgot Password
  // forgotPassword(email: string, newPassword: string): Observable<any> {
  //   return this.http.post(`${this.BASE_URL}/forgot-password`, { email, newPassword });
  // }

  // Update Password
  // updatePassword(oldPassword: string, newPassword: string): Observable<any> {
  //   return this.http.put(`${this.BASE_URL}/update-password`, { oldPassword, newPassword });
  // }
}
