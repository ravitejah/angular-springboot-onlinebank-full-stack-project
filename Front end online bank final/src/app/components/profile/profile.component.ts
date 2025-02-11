import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {
  user: any = { name: '', email: '', mobileNumber: '' };
  isLoading = true;

  constructor(private apiService: ApiService, private router: Router) {}

  ngOnInit() {
    this.apiService.getUserProfile().subscribe({
      next: (data) => {
        this.user = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching profile:', err);
        this.isLoading = false;
      },
    });
  }

  goToChangePassword() {
    this.router.navigate(['/change-password']);
  }
}
