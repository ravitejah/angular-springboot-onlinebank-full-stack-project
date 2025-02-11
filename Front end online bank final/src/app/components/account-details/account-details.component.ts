import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-account-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.css'],
})
export class AccountDetailsComponent implements OnInit {
  account: any = null;
  errorMessage: string = '';

  constructor(private apiService: ApiService, private route: ActivatedRoute) {}

  // ngOnInit() {
  //   const accountId = this.route.snapshot.paramMap.get('id');
  //   if (accountId) {
  //     this.apiService.getAccountById(+accountId).subscribe({
  //       next: (data) => {
  //         this.account = data;
  //       },
  //       error: (err) => {
  //         this.errorMessage = 'Failed to fetch account details';
  //         console.error('Error fetching account:', err);
  //       },
  //     });
  //   }
  // }
  ngOnInit() {
    const accountIdOrNumber = this.route.snapshot.paramMap.get('id');
  
    if (accountIdOrNumber) {
      if (!isNaN(+accountIdOrNumber)) {
        // ✅ If it's a number, fetch by account ID
        const accountId = +accountIdOrNumber;
        console.log('Fetching account details by ID:', accountId);
        this.apiService.getAccountById(accountId).subscribe({
          next: (data) => {
            this.account = data;
          },
          error: (err) => {
            this.errorMessage = 'Failed to fetch account details';
            console.error(' Error fetching account:', err);
          },
        });
      } else {
        // ✅ If it's NOT a number, fetch by account number
        console.log(' Fetching account details by Account Number:', accountIdOrNumber);
        this.apiService.getAccountByAccountNumber(accountIdOrNumber).subscribe({
          next: (data) => {
            this.account = data;
          },
          error: (err) => {
            this.errorMessage = 'Failed to fetch account details';
            console.error(' Error fetching account:', err);
          },
        });
      }
    }
  }
}
