import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../core/services/auth.service'; // Update path if needed
import { HttpErrorResponse } from '@angular/common/http';
import { ServicesService } from '../core/services/services.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  standalone: true,
})
export class LoginComponent {
  toggleTheme() {
    const body = document.body;
    body.classList.toggle('dark-theme');
    body.classList.toggle('light-theme');
  }

  loginForm: FormGroup;
  isLoading = false;
  errorMessage: string | null = null;
  lockIconClass = 'fa-solid fa-lock';
  showPassword = false;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private authService: AuthService,
    private servicesService: ServicesService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  redirectRegister(): void {
    this.router.navigate(['/register']);
  }

  get email() {
    return this.loginForm.get('email')!;
  }

  get password() {
    return this.loginForm.get('password')!;
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      Object.keys(this.loginForm.controls).forEach(key => {
        this.loginForm.get(key)?.markAsTouched();
      });
      return;
    }

    this.isLoading = true;
    this.errorMessage = null;

    // this.servicesService.login(this.loginForm.value).subscribe({
    //   next: (response: any) => {
    //     console.log('Login successful', response);
    //     this.authService.storeToken(response.token);

    //     if (response.user) {
    //       this.authService.storeUserData(response.user);
    //     }

    //     this.lockIconClass = 'fa-solid fa-unlock';

    //     this.isLoading = false;
    //     this.router.navigate(['/home']);
    //   },

    this.servicesService.login(this.loginForm.value).subscribe({
      next: (response) => {
        console.log('Login successful', response);

        const token = response.token;
        const user = response.user;

        // 🔐 Store token and user
        this.authService.storeToken(token);
        this.authService.storeUserData(user);

        // ✅ Navigate to home or wherever
        this.router.navigate(['/home']);
      },
      
      error: (error: HttpErrorResponse) => {
        this.isLoading = false;
        console.error('Login failed:', error);
        if (error.status === 401) {
          this.errorMessage = 'Invalid credentials';
        } else if (error.status >= 500) {
          this.errorMessage = 'Server error: Please try again later';
        } else {
          this.errorMessage = error.error?.message || 'An unexpected error occurred';
        }
      }
    });
  }
}