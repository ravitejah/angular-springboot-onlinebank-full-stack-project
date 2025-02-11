import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  user = { name: '', email: '', password: '', mobileNumber: '', aadharNumber: '' };
  successMessage: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  register() {
    this.authService.register(this.user).subscribe({
      next: (response: string) => {
        this.successMessage = response; // ✅ Store API response message
        this.errorMessage = ''; // Clear error messages
        setTimeout(() => {
          this.router.navigate(['/login']); // Redirect after success
        }, 2000);
      },
      error: (err) => {
        this.errorMessage = err.error || 'Registration failed. Please try again.';
        this.successMessage = ''; // Clear success message
      },
    });
  }

}
