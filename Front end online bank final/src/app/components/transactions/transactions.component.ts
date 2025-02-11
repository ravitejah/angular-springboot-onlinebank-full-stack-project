import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css'],
})
export class TransactionsComponent implements OnInit {
  transactions: any[] = [];
  filteredTransactions: any[] = [];
  accounts: any[] = [];
  transactionRequest = { accountId: '', transactionType: 'DEBIT', amount: 0 };
  successMessage: string = '';
  errorMessage: string = '';
  selectedFilter: string = 'ALL';
  sortOrder: string = 'desc'; // Default to latest first

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.fetchTransactions();
    this.fetchAccounts();
  }

  // ✅ Fetch transactions and apply filtering & sorting
  fetchTransactions() {
    this.apiService.getAllTransactions().subscribe({
      next: (data) => {
        this.transactions = data;
        this.applyFiltersAndSorting();
      },
      error: () => {
        this.errorMessage = 'Error fetching transactions.';
      },
    });
    setTimeout(() => {
      this.successMessage = '';
      this.errorMessage = '';
    }, 5000);
  }

  // ✅ Fetch accounts
  fetchAccounts() {
    this.apiService.getAllAccounts().subscribe({
      next: (data) => {
        this.accounts = data;
      },
      error: () => {
        this.errorMessage = 'Error fetching accounts.';
      },
    });
  }

  // ✅ Perform a transaction
  performTransaction() {
    this.successMessage = '';
    this.errorMessage = '';

    if (!this.transactionRequest.accountId) {
      this.errorMessage = 'Please select an account!';
      return;
    }

    const accountId = Number(this.transactionRequest.accountId);
    if (isNaN(accountId)) {
      this.errorMessage = 'Invalid account selection!';
      return;
    }

    this.apiService.performTransaction(accountId, this.transactionRequest).subscribe({
      next: () => {
        this.successMessage = 'Transaction successful!';
        this.transactionRequest = { accountId: '', transactionType: 'DEBIT', amount: 0 };
        this.fetchTransactions();
        this.fetchAccounts();
      },
      error: (err) => {
        if (err.status === 400 && err.error === "Insufficient balance") {
          this.errorMessage = '❌ Insufficient balance!';
        } else {
          this.errorMessage = '❌ Error performing transaction!';
        }
      },
    });
    setTimeout(() => {
      this.successMessage = '';
      this.errorMessage = '';
    }, 5000);
  }

  // ✅ Apply filters and sorting together
  applyFiltersAndSorting() {
    let filtered = this.transactions.filter((t) =>
      this.selectedFilter === 'ALL' ? true : t.transactionType === this.selectedFilter
    );

    this.filteredTransactions = filtered.sort((a, b) => {
      return this.sortOrder === 'desc'
        ? new Date(b.transactionDateTime).getTime() - new Date(a.transactionDateTime).getTime()
        : new Date(a.transactionDateTime).getTime() - new Date(b.transactionDateTime).getTime();
    });
  }

  // ✅ Change filter and apply sorting
  filterTransactions() {
    this.applyFiltersAndSorting();
  }

  // ✅ Toggle sorting order and reapply filters & sorting
  toggleSortOrder() {
    this.sortOrder = this.sortOrder === 'desc' ? 'asc' : 'desc';
    this.applyFiltersAndSorting();
  }
}