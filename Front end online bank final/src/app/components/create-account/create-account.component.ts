import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-create-account',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.css'],
})
export class CreateAccountComponent implements OnInit {
  userId: number | null = null;
  accountData = { balance: 0 };
  errorMessage: string = '';
  successMessage: string = '';
  createdAccount: any = null; // Store the created account details

  constructor(private apiService: ApiService, private router: Router) {}

  ngOnInit() {
    this.apiService.getUserProfile().subscribe({
      next: (user) => {
        this.userId = user.id; // Ensure the backend returns the user ID
        console.log(' User ID fetched:', this.userId);
      },
      error: () => {
        this.errorMessage = 'Failed to fetch user details.';
      },
    });
  }

  createAccount() {
    if (!this.userId) {
      this.errorMessage = 'User ID not found.';
      return;
    }

    this.apiService.createAccount(this.userId, this.accountData).subscribe({
      next: (response) => {
        this.successMessage = '✅ Account created successfully!';
        this.createdAccount = response; // Store the created account details
        this.resetForm(); // Reset form after successful creation
      },
      error: (err) => {
        this.errorMessage = '❌ Failed to create account';
        console.error('Error creating account:', err);
      },
    });
  }

  resetForm() {
    this.accountData = { balance: 0 };
  }
}