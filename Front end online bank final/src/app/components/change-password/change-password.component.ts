import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css'],
})
export class ChangePasswordComponent {
  oldPassword = '';
  newPassword = '';
  confirmPassword = '';
  errorMessage = '';
  successMessage = '';

  constructor(private apiService: ApiService, private router: Router) {}

  changePassword() {
    this.successMessage = '';
    this.errorMessage = '';
  
    if (this.newPassword !== this.confirmPassword) {
      this.errorMessage = 'New password and confirm password do not match.';
      return;
    }
  
    this.apiService.changePassword(this.oldPassword, this.newPassword).subscribe({
      next: (response: string) => { // ✅ Response is now plain text
        this.successMessage = response; // Directly use the response text
        this.errorMessage = ''; // Clear previous errors
        setTimeout(() => this.router.navigate(['/profile']), 2000);
      },
      error: (err) => {
        console.error('❌ Error:', err);
        this.errorMessage = 'Incorrect old password or failed to change.';
        this.successMessage = ''; // Clear previous success message
      },
    });
  }

 
}
