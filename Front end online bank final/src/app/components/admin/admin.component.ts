import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css'],
})
export class AdminComponent implements OnInit {
  users: any[] = [];
  accounts: any[] = [];
  transactions: any[] = [];
  loans: any[] = [];
  selectedUser: any = null;
  selectedLoan: any = null;
  newBalance: number = 0;
  successMessage: string = '';
  errorMessage: string = '';
  selectedFilter: string = 'ALL';
  filteredTransactions: any[] = [];
  sortOrder: string = 'desc';
  activeTab: string = 'users'; 
  showLoanModal:boolean=false;
 

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.fetchUsers();
    this.fetchAccounts();
    this.fetchTransactions();
    this.fetchLoans();
  }

  // ✅ Fetch Users
  fetchUsers() {
    this.apiService.getAllUsers().subscribe({
      next: (data) => (this.users = data),
      error: () => (this.errorMessage = 'Error fetching users!'),
    });
  }

  // ✅ Fetch Accounts
  fetchAccounts() {
    this.apiService.getAllAccountsAdmin().subscribe({
      next: (data) => (this.accounts = data),
      error: () => (this.errorMessage = 'Error fetching accounts!'),
    });
  }

  // ✅ Fetch Transactions
  fetchTransactions() {
    this.apiService.getAllTransactionsAdmin().subscribe({
      next: (data) => (this.transactions = data),
      
      error: () => (this.errorMessage = 'Error fetching transactions!'),
    });
  }

  // ✅ Fetch Loans
  fetchLoans() {
    this.apiService.getAllLoansAdmin().subscribe({
      next: (data) => (this.loans = data),
      error: () => (this.errorMessage = 'Error fetching loans!'),
    });
  }

  // ✅ Select User for Editing
  selectUser(user: any) {
    this.selectedUser = { ...user };
    this.successMessage = '';
    this.errorMessage = '';
  }

  // ✅ Update User
  updateUser() {
    if (this.selectedUser) {
      this.apiService.updateUser(this.selectedUser.id, this.selectedUser).subscribe({
        next: () => {
          this.successMessage = '✅ User updated successfully!';
          this.fetchUsers();
          this.selectedUser = null;
        },
        error: () => (this.errorMessage = '❌ Error updating user!'),
      });
    }
    setTimeout(() => {
      this.successMessage = '';
      this.errorMessage = '';
    }, 5000);
  }

  // ✅ Delete User
  deleteUser(userId: number) {
    if (confirm('Are you sure you want to delete this user?')) {
      this.apiService.deleteUser(userId).subscribe({
        next: () => {
          this.successMessage = '✅ User deleted successfully!';
          this.fetchUsers();
          this.fetchAccounts();
          this.filterTransactions();
          this.fetchLoans();
        },
        error: () => (this.errorMessage = '❌ Error deleting user!'),
      });
    }
    setTimeout(() => {
      this.successMessage = '';
      this.errorMessage = '';
    }, 5000);
  }


  // ✅ Update Account Balance
  updateBalance(account: any) {
    if (account.newBalance > 0) {
      this.apiService.updateAccountBalance(account.id, account.newBalance).subscribe({
        next: () => {
          this.successMessage = '✅ Balance updated successfully!';
          this.fetchAccounts();
          account.newBalance = null;
        },
        error: () => (this.errorMessage = '❌ Error updating balance!'),
      });
    }
    setTimeout(() => {
      this.successMessage = '';
      this.errorMessage = '';
    }, 5000);
  }

  // ✅ Filter Transactions
  filterTransactions() {
    this.applyFiltersAndSorting();
  }
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
  toggleSortOrder() {
    this.sortOrder = this.sortOrder === 'desc' ? 'asc' : 'desc';
    this.applyFiltersAndSorting();
  }

   // ✅ Open Loan Approval Modal
   openLoanModal(loan: any) {
    this.selectedLoan = loan;
    this.showLoanModal = true;
  }

  // ✅ Close Loan Modal
  closeLoanModal() {
    this.showLoanModal = false;
    this.selectedLoan = null;
  }

  // ✅ Approve or Reject Loan
  approveLoan(approve: boolean) {
    if (!this.selectedLoan) return;
    this.apiService.approveLoan(this.selectedLoan.id, approve).subscribe({
      next: () => {
        this.successMessage = approve ? '✅ Loan Approved!' : '🚫 Loan Rejected!';
        this.fetchLoans();
        this.fetchAccounts();
        this.fetchTransactions();
        this.closeLoanModal();
      },
      error: () => (this.errorMessage = '❌ Error processing loan approval!'),
    });
    setTimeout(() => {
      this.successMessage = '';
      this.errorMessage = '';
    }, 5000);
  }

  // ✅ Switch Tab in Mini Navigation
  switchTab(tab: string) {
    this.activeTab = tab;
  }
}
