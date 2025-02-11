import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AccountsComponent } from './components/accounts/accounts.component';
import { LoansComponent } from './components/loans/loans.component';
import { TransactionsComponent } from './components/transactions/transactions.component';
import { AdminComponent } from './components/admin/admin.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { AccountDetailsComponent } from './components/account-details/account-details.component';
import { CreateAccountComponent } from './components/create-account/create-account.component';
import { CheckBalanceComponent } from './components/check-balance/check-balance.component';

export const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'change-password', component: ChangePasswordComponent },
  { path: 'accounts', component: AccountsComponent },
  { path: 'account-details/:id', component: AccountDetailsComponent },
  { path: 'create-account', component: CreateAccountComponent },
  { path: 'check-balance', component: CheckBalanceComponent },
  { path: 'loans', component: LoansComponent },
  { path: 'transactions', component: TransactionsComponent },
  { path: 'admin', component: AdminComponent },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: '**', redirectTo: '/home' },
];
