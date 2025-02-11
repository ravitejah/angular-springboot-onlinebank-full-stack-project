import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent {
  isAuthenticated: boolean = false;
  role: string = '';

  constructor(private authService: AuthService) {
    this.updateAuthStatus();
  }

  updateAuthStatus() {
    const token = localStorage.getItem('token');
    if (token) { // ✅ Checking if token exists before decoding
      this.isAuthenticated = true;
      try {
        const payload = JSON.parse(atob(token.split('.')[1])); // Decode JWT safely
        this.role = payload?.role || ''; // Ensure role exists
      } catch (error) {
        console.error('Invalid token format', error);
        this.isAuthenticated = false;
        this.role = '';
      }
    } else {
      this.isAuthenticated = false;
      this.role = '';
    }
  }

  logout() {
    this.authService.logout();
    this.isAuthenticated = false;
    this.role = '';
  }
}
