import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-check-balance',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './check-balance.component.html',
  styleUrls: ['./check-balance.component.css'],
})
export class CheckBalanceComponent implements OnInit {
  accounts: any[] = []; // List of user accounts
  selectedAccountNumber: string | null = null; // Selected account number
  balance: number | null = null;
  errorMessage: string = '';

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.fetchUserAccounts();
  }

  // Fetch all accounts of the logged-in user
  fetchUserAccounts() {
    this.apiService.getAllAccounts().subscribe({
      next: (data) => {
        this.accounts = data;
      },
      error: (err) => {
        this.errorMessage = 'Failed to fetch accounts';
        console.error('Error fetching accounts:', err);
      },
    });
  }

  // checkBalance() {
  //   if (!this.selectedAccountNumber) {
  //     this.errorMessage = 'Please select an account!';
  //     return;
  //   }

  //   this.apiService.getAccountBalance(this.selectedAccountNumber).subscribe({
  //     next: (data) => {
  //       this.balance = data;
  //       this.errorMessage = '';
  //     },
  //     error: (err) => {
  //       this.errorMessage = 'Failed to fetch balance';
  //       console.error('Error fetching balance:', err);
  //     },
  //   });
  // }

  checkBalance() {
    if (!this.selectedAccountNumber) {
      this.errorMessage = 'Please select an account!';
      return;
    }
  
    console.log('Checking balance for account:', this.selectedAccountNumber); // Debugging log
  
    this.apiService.getAccountBalance(this.selectedAccountNumber).subscribe({
      next: (data) => {
        this.balance = data;
        this.errorMessage = '';
      },
      error: (err) => {
        this.errorMessage = 'Failed to fetch balance!';
        console.error('Error fetching balance:', err);
      },
    });
  }
  
}