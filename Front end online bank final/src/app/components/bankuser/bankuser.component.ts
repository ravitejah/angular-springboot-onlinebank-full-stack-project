// bankuser.component.ts
import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common'; // Import CommonModule for NgIf

@Component({
  selector: 'app-bankuser',
  imports: [ CommonModule],
  standalone: true,
  templateUrl: './bankuser.component.html',
  styleUrls: ['./bankuser.component.css'],
})
export class BankUserComponent implements OnInit {
  bankUser: any;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.apiService.getCurrentUser().subscribe(
      (response) => {
        this.bankUser = response;
      },
      (error) => {
        alert('Failed to load user details');
      }
    );
  }
}
