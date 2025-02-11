import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css'],
})
export class AccountsComponent implements OnInit {
  accounts: any[] = [];
  errorMessage: string = '';

  constructor(private apiService: ApiService, private router: Router) {}

  ngOnInit() {
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

  viewAccount(account: any) {
    if (!account || (!account.id && !account.accountNumber)) {
      console.error('Account ID is undefined or invalid:',account);
      return;
    }
    const accountIdentifier=account.id?account.id:account.accountNumber;
    this.router.navigate(['/account-details', accountIdentifier]); // ✅ Pass the correct ID
  }
}
