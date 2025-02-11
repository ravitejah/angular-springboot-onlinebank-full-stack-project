import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private BASE_URL = 'http://localhost:8080'; // Backend URL

  constructor(private http: HttpClient) {}

  /** ==============================
   *  AUTHENTICATION API CALLS
   *  ============================== */
  
  login(data: { email: string; password: string }): Observable<any> {
    return this.http.post(`${this.BASE_URL}/auth/login`, data);
  }

 
  
  
  
  

  // forgotPassword(email: string, newPassword: string): Observable<any> {
  //   return this.http.post(`${this.BASE_URL}/auth/forgot-password`, { email, newPassword });
  // }

  // changePassword(oldPassword: string, newPassword: string): Observable<any> {
  //   const body = { oldPassword, newPassword }; // Send as JSON
  
  //   return this.http.put(`${this.BASE_URL}/auth/update-password`, body, {
  //     headers: new HttpHeaders({
  //       Authorization: `Bearer ${localStorage.getItem('token')}`,
  //       'Content-Type': 'application/json', // Ensure proper request format
  //     }),
  //   });
  // }

  changePassword(oldPassword: string, newPassword: string): Observable<any> {
    const body = { oldPassword, newPassword }; // Send as JSON
  
    return this.http.put(`${this.BASE_URL}/auth/update-password`, body, {
      headers: new HttpHeaders({
        Authorization: `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'application/json', // ✅ Ensure JSON format
      }),
      responseType: 'text', // ✅ Expect plain text response (Fix the error)
    });
  }
  
  
  
  

  getUserProfile(): Observable<any> {
    return this.http.get(`${this.BASE_URL}/auth/me`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    });
  }
  

  /** ==============================
   *  ACCOUNT API CALLS
   *  ============================== */

  // 🔹 Get all accounts for the logged-in user
  getAllAccounts(): Observable<any[]> {
    return this.http.get<any[]>(`${this.BASE_URL}/accounts`, this.getAuthHeaders());
  }

 // ✅ Fetch account by ID
getAccountById(accountId: number): Observable<any> {
  return this.http.get(`${this.BASE_URL}/accounts/${accountId}`, {
    headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
  });
}

// ✅ Fetch account by Account Number
getAccountByAccountNumber(accountNumber: string): Observable<any> {
  return this.http.get(`${this.BASE_URL}/accounts/by-account-number/${accountNumber}`, {
    headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
  });
}

  // 🔹 Get account balance
  // getAccountBalance(accountNumber: string): Observable<number> {
  //   return this.http.get<number>(`${this.BASE_URL}/accounts/balance/${accountNumber}`, this.getAuthHeaders());
  // }

  // 🔹 Get account balance
  getAccountBalance(accountNumber: string): Observable<number> {
    const encodedAccountNumber = encodeURIComponent(accountNumber); // Encode URL parameter
    return this.http.get<number>(
      `${this.BASE_URL}/accounts/balance/${encodedAccountNumber}`,
      this.getAuthHeaders()
    );
  }
  

  // 🔹 Create a new account
  createAccount(userId: number, accountData: any): Observable<any> {
    return this.http.post(`${this.BASE_URL}/accounts/${userId}`, accountData, this.getAuthHeaders());
  }

  // 🔹 Authorization Headers
  // private getAuthHeaders() {
  //   const token = localStorage.getItem('token');
  //   return {
  //     headers: new HttpHeaders({
  //       Authorization: `Bearer ${token}`,
  //     }),
  //   };
  // }

  // 🔹 Authorization Headers
  private getAuthHeaders() {
    const token = localStorage.getItem('token');
  
    if (!token) {
      console.error('❌ No authentication token found!');
      return {};
    }
  
    console.log('✅ Sending Authorization Header:', `Bearer ${token}`);
  
    return {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`,
      }),
    };
  }
  
  /** ==============================
   *  LOAN API CALLS
   *  ============================== */

  // ✅ Fetch all loans for the current user
getAllLoans(): Observable<any[]> {
  return this.http.get<any[]>(`${this.BASE_URL}/loans`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    },
  });
}

// ✅ Apply for a loan
applyLoan(data: any): Observable<any> {
  return this.http.post(`${this.BASE_URL}/loans`, data, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    },
  });
}

// ✅ Update loan details
updateLoan(loanId: number, data: any): Observable<any> {
  return this.http.put(`${this.BASE_URL}/loans/${loanId}`, data, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    },
  });
}

// ✅ Delete a loan
deleteLoan(loanId: number): Observable<any> {
  return this.http.delete(`${this.BASE_URL}/loans/${loanId}`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    },
    responseType: 'text' as 'json', // ✅ This tells Angular to expect a text response
  });
}


  /** ==============================
   *  TRANSACTION API CALLS
   *  ============================== */
// ✅ Fetch all transactions for the current user
getAllTransactions(): Observable<any[]> {
  return this.http.get<any[]>(`${this.BASE_URL}/transactions`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    },
  });
}

// ✅ Perform a transaction
performTransaction(accountId: number, data: any): Observable<any> {
  return this.http.post(`${this.BASE_URL}/transactions/${accountId}`, data, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    },
  });
}

  /** ==============================
   *  ADMIN API CALLS
   *  ============================== */
// ✅ Admin APIs

 // ✅ Get all users (Admin)
 getAllUsers(): Observable<any> {
  return this.http.get(`${this.BASE_URL}/admin/users`, this.getAuthHeaders());
}

// ✅ Get all accounts (Admin)
getAllAccountsAdmin(): Observable<any> {
  return this.http.get(`${this.BASE_URL}/admin/accounts`, this.getAuthHeaders());
}

// ✅ Get all transactions (Admin)
getAllTransactionsAdmin(): Observable<any> {
  return this.http.get(`${this.BASE_URL}/admin/transactions`, this.getAuthHeaders());
}

// ✅ Update user details (Admin)
updateUser(userId: number, updatedUser: any): Observable<any> {
  return this.http.put(`${this.BASE_URL}/admin/users/${userId}`, updatedUser, this.getAuthHeaders());
}

// ✅ Delete user (Admin)
deleteUser(userId: number): Observable<any> {
  return this.http.delete(`${this.BASE_URL}/admin/users/${userId}`, { responseType: 'text' });
}

// ✅ Update account balance (Admin)
updateAccountBalance(accountId: number, newBalance: number): Observable<any> {
  return this.http.put(`${this.BASE_URL}/admin/accounts/${accountId}/balance?newBalance=${newBalance}`, null, this.getAuthHeaders());
}

// ✅ Get all loans (Admin)
getAllLoansAdmin(): Observable<any> {
  return this.http.get(`${this.BASE_URL}/admin/loans`, this.getAuthHeaders());
}

// ✅ Get loan details by ID (Admin)
getLoanById(loanId: number): Observable<any> {
  return this.http.get(`${this.BASE_URL}/admin/loans/${loanId}`, this.getAuthHeaders());
}

// ✅ Approve or reject loan (Admin)
approveLoan(loanId: number, approve: boolean): Observable<any> {
  return this.http.put(`${this.BASE_URL}/admin/approve-loan/${loanId}?approve=${approve}`, null, { responseType: 'text' });
}

// ✅ Helper function to get Authorization Headers
// private getAuthHeaders() {
//   return {
//     headers: new HttpHeaders({
//       Authorization: `Bearer ${localStorage.getItem('token')}`,
//     }),
//   };
// }
// getAllUsers(): Observable<any[]> {
//   return this.http.get<any[]>(`${this.BASE_URL}/admin/users`);
// }

// getUserById(userId: number): Observable<any> {
//   return this.http.get<any>(`${this.BASE_URL}/admin/users/${userId}`);
// }

// getAllAccountsAdmin(): Observable<any[]> {
//   return this.http.get<any[]>(`${this.BASE_URL}/admin/accounts`);
// }

// updateAccountBalance(accountId: number, newBalance: number): Observable<any> {
//   return this.http.put<any>(`${this.BASE_URL}/admin/accounts/${accountId}/balance?newBalance=${newBalance}`, {});
// }

// getAllTransactionsAdmin(): Observable<any[]> {
//   return this.http.get<any[]>(`${this.BASE_URL}/admin/transactions`);
// }

// updateUser(userId: number, userData: any): Observable<any> {
//   return this.http.put<any>(`${this.BASE_URL}/admin/users/${userId}`, userData);
// }

// deleteUser(userId: number): Observable<any> {
//   return this.http.delete(`${this.BASE_URL}/admin/users/${userId}`, { responseType: 'text' });
// }

}
