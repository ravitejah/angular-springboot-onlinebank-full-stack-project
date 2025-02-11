import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-loans',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './loans.component.html',
  styleUrls: ['./loans.component.css'],
})
export class LoansComponent implements OnInit {
  loans: any[] = [];   // Stores user loans
  loanRequest = { amount: 0, status: 'Pending' };  // Loan form data
  isUpdating: boolean = false;
  updateLoanId: number | null = null;
  errorMessage: string = '';
  successMessage: string = '';  // Success message for UI feedback
  showModal: boolean = false;
  modalAction: string = ''; // "apply" | "update" | "delete"
  modalLoanId: number | null = null;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.fetchLoans();
  }

  // ✅ Fetch all loans for the logged-in user
  fetchLoans() {
    this.apiService.getAllLoans().subscribe({
      next: (data) => {
        this.loans = data;
        this.errorMessage = ''; // Only clear errors on fetching loans
      },
      error: () => {
        this.errorMessage = 'Failed to load loans. Try again later.';
      },
    });
    setTimeout(() => {
      this.successMessage = '';
      this.errorMessage = '';
    }, 5000);
  }

  // ✅ Show Confirmation Modal
  openModal(action: string, loanId: number | null = null) {
    this.modalAction = action;
    this.modalLoanId = loanId;
    this.showModal = true;
  }

  // ✅ Close Modal
  closeModal() {
    this.showModal = false;
  }

  // ✅ Apply for a Loan
  confirmApplyLoan() {
    if (this.loanRequest.amount < 500) {
      this.errorMessage = 'Loan amount must be at least 500!';
      return;
    }

    this.apiService.applyLoan(this.loanRequest).subscribe({
      next: () => {
        this.successMessage = 'Loan applied successfully!';
        this.errorMessage = ''; // Clear error message
        this.loanRequest = { amount: 0, status: 'Pending' };
        this.fetchLoans();
        this.closeModal();
      },
      error: () => {
        this.errorMessage = 'Error applying for loan. Try again.';
      },
    });
    setTimeout(() => {
      this.successMessage = '';
      this.errorMessage = '';
    }, 5000);
  }

  // ✅ Enable loan update mode
  editLoan(loan: any) {
    this.isUpdating = true;
    this.updateLoanId = loan.id;
    this.loanRequest.amount = loan.amount;
    this.errorMessage = ''; // Clear error message when switching modes
  }

  // ✅ Update loan details
  confirmUpdateLoan() {
    if (!this.updateLoanId) {
      this.errorMessage = 'No loan selected for update.';
      return;
    }

    if (this.loanRequest.amount < 500) {
      this.errorMessage = 'Loan amount must be at least 500!';
      return;
    }

    this.apiService.updateLoan(this.updateLoanId, this.loanRequest).subscribe({
      next: () => {
        this.successMessage = 'Loan updated successfully!';
        this.errorMessage = ''; // Clear error message
        this.isUpdating = false;
        this.updateLoanId = null;
        this.loanRequest = { amount: 0, status: 'Pending' };
        this.fetchLoans();
        this.closeModal();
      },
      error: () => {
        this.errorMessage = 'Error updating loan. Try again.';
      },
    });
    setTimeout(() => {
      this.successMessage = '';
      this.errorMessage = '';
    }, 5000);
  }

  // ✅ Delete a loan
  confirmDeleteLoan() {
    if (!this.modalLoanId) return;

    this.apiService.deleteLoan(this.modalLoanId).subscribe({
      next: () => {
        this.successMessage = 'Loan deleted successfully!';
        this.errorMessage = ''; // Clear error message
        this.fetchLoans();
        this.closeModal();
      },
      error: () => {
        this.errorMessage = 'Error deleting loan.';
      },
    });
    setTimeout(() => {
      this.successMessage = '';
      this.errorMessage = '';
    }, 5000);
  }
}