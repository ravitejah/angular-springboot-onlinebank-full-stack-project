import { Component } from '@angular/core';
import { FormsModule, NgModel } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common'; // Import CommonModule for NgIf

@Component({
  selector: 'app-login',
  imports: [FormsModule, CommonModule],
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  loginData = { email: '', password: '' };
  errorMessage: string = ''; // ✅ Added errorMessage property

  constructor(private apiService: ApiService) {}

  login() {
    this.apiService.login(this.loginData).subscribe(
      (response: any) => {
        localStorage.setItem('token', response.token);
        window.location.href = '/home';
      },
      (error) => {
        this.errorMessage = 'Login failed. Please check your credentials.'; // ✅ Set error message
      }
    );
  }
}
