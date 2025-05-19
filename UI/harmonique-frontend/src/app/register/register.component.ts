// import { Component } from '@angular/core';
// import { Router } from '@angular/router';
// import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
// import { CommonModule } from '@angular/common';
// import { RouterModule } from '@angular/router';
// import { ServicesService } from '../../services.service';
// import { HttpErrorResponse } from '@angular/common/http';

// @Component({
//   selector: 'app-register',
//   standalone: true,
//   imports: [CommonModule, RouterModule, ReactiveFormsModule],
//   templateUrl: './register.component.html',
//   styleUrls: ['./register.component.scss'],
// })
// export class RegisterComponent {
//   registrationForm: FormGroup;
//   isLoading = false;
//   errorMessage: string | null = null;
//   successMessage: string | null = null;
//   lockIconClass = 'fa-solid fa-lock';
//   showPassword = false;
//   showConfirmPassword = false;


//   constructor(
//     private fb: FormBuilder,
//     private router: Router,
//     private servicesService: ServicesService
//   ) {
//     this.registrationForm = this.fb.group({
//       fullName: ['', [Validators.required, Validators.minLength(3)]],
//       email: ['', [Validators.required, Validators.email]],
//       password: ['', [Validators.required, Validators.minLength(6)]],
//       confirmPassword: ['', [Validators.required]],
//     }, { validator: this.passwordMatchValidator });
//   }

//   // Custom validator for password matching
//   private passwordMatchValidator(form: FormGroup) {
//     return form.get('password')?.value === form.get('confirmPassword')?.value
//       ? null
//       : { mismatch: true };
//   }

//   onSubmit() {
//     if (this.registrationForm.invalid) {
//       this.registrationForm.markAllAsTouched();
//       return;
//     }

//     this.isLoading = true;
//     this.errorMessage = null;
//     this.successMessage = null;

//     const { fullName, email, password } = this.registrationForm.value;

//     this.servicesService.register({ name: fullName, email, password }).subscribe({
//       next: (response) => {
//         this.isLoading = false;
//         this.successMessage = 'Registration successful! Redirecting to login...';
//         setTimeout(() => {
//           this.router.navigate(['/login']);
//         }, 2000);
//       },
//       error: (error: HttpErrorResponse) => {
//         this.isLoading = false;
//         if (error.status === 400 || error.status === 409) {
//           this.errorMessage = error.error?.message || 'Email already exists';
//         } else if (error.status === 0) {
//           this.errorMessage = 'Network error: Please check your connection';
//         } else {
//           this.errorMessage = 'Registration failed. Please try again.';
//         }
//       }
//     });
//   }

//   get f() {
//     return this.registrationForm.controls;
//   }
// }

// 2nd One

// import { Component } from '@angular/core';
// import { Router } from '@angular/router';
// import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
// import { CommonModule } from '@angular/common';
// import { RouterModule } from '@angular/router';
// import { ServicesService } from '../../services.service';
// import { HttpErrorResponse } from '@angular/common/http';

// @Component({
//   selector: 'app-register',
//   standalone: true,
//   imports: [CommonModule, RouterModule, ReactiveFormsModule],
//   templateUrl: './register.component.html',
//   styleUrls: ['./register.component.scss'],
// })
// export class RegisterComponent {
//   registrationForm: FormGroup;
//   isLoading = false;
//   errorMessage: string | null = null;
//   successMessage: string | null = null;
//   lockIconClass = 'fa-solid fa-lock';
//   showPassword = false;
//   showConfirmPassword = false;

//   selectedFile: File | null = null; ///
//   previewUrl: string | ArrayBuffer | null = null; ///

//   constructor(
//     private fb: FormBuilder,
//     private router: Router,
//     private servicesService: ServicesService
//   ) {
//     this.registrationForm = this.fb.group({
//       firstName: ['', [Validators.required]],
//       lastName: [''],
//       username: ['', [Validators.required]],
//       email: [''],
//       phoneNo: [''],
//       password: ['', [Validators.required, Validators.minLength(6)]],
//       confirmPassword: ['', [Validators.required]],
//       profilePictureUrl: [''],
//       about: [''],
//       location: [''],
//     }, { validator: this.passwordMatchValidator });
//   }

//   // ✅ Handle file input
//   onFileSelected(event: Event): void {
//     const input = event.target as HTMLInputElement;
//     if (input.files && input.files[0]) {
//       this.selectedFile = input.files[0];

//       const reader = new FileReader();
//       reader.onload = () => {
//         this.previewUrl = reader.result;
//       };
//       reader.readAsDataURL(this.selectedFile);
//     }
//   }

//   // Custom validator for password matching
//   private passwordMatchValidator(form: FormGroup) {
//     return form.get('password')?.value === form.get('confirmPassword')?.value
//       ? null
//       : { mismatch: true };
//   }

//   onSubmit() {
//     if (this.registrationForm.invalid) {
//       this.registrationForm.markAllAsTouched();
//       return;
//     }

//     const formValues = this.registrationForm.value;

//     // Ensure at least one of email or phoneNo is provided
//     if (!formValues.email && !formValues.phoneNo) {
//       this.errorMessage = 'Please provide either Email or Phone Number.';
//       return;
//     }

//     this.isLoading = true;
//     this.errorMessage = null;
//     this.successMessage = null;

//     const userPayload = {
//       firstName: formValues.firstName,
//       lastName: formValues.lastName || null,
//       username: formValues.username,
//       password: formValues.password,
//       email: formValues.email || null,
//       phoneNo: formValues.phoneNo || null,
//       profilePictureUrl: formValues.profilePictureUrl || null,
//       about: formValues.about || null,
//       location: formValues.location || null,
//     };

//     //   this.servicesService.register(userPayload).subscribe({
//     //     next: (response) => {
//     //       this.isLoading = false;
//     //       this.successMessage = 'Registration successful! Redirecting to login...';
//     //       setTimeout(() => {
//     //         this.router.navigate(['/login']);
//     //       }, 2000);
//     //     },
//     //     error: (error: HttpErrorResponse) => {
//     //       this.isLoading = false;
//     //       if (error.status === 400 || error.status === 409) {
//     //         this.errorMessage = error.error?.message || 'Registration failed: Bad Request';
//     //       } else if (error.status === 0) {
//     //         this.errorMessage = 'Network error: Please check your connection';
//     //       } else {
//     //         this.errorMessage = 'Registration failed. Please try again.';
//     //       }
//     //     }
//     //   });
//     // }

//     // Step 1: Register user
//     this.servicesService.register(userPayload).subscribe({
//       next: () => {
//         // Step 2: Upload profile picture if selected
//         if (this.selectedFile) {
//           this.servicesService.uploadProfilePicture(this.selectedFile).subscribe({
//             next: () => {
//               this.successMessage = 'Registration complete with profile picture!';
//               this.navigateToLogin();
//             },
//             error: () => {
//               this.successMessage = 'User registered. Failed to upload picture.';
//               this.navigateToLogin();
//             }
//           });
//         } else {
//           this.successMessage = 'Registration successful!';
//           this.navigateToLogin();
//         }
//       },
//       error: (error: HttpErrorResponse) => {
//         this.isLoading = false;
//         if (error.status === 400 || error.status === 409) {
//           this.errorMessage = error.error?.message || 'Registration failed: Bad Request';
//         } else if (error.status === 0) {
//           this.errorMessage = 'Network error: Please check your connection';
//         } else {
//           this.errorMessage = 'Registration failed. Please try again.';
//         }
//       }
//     });
//   }

//   private navigateToLogin(): void {
//     this.isLoading = false;
//     setTimeout(() => {
//       this.router.navigate(['/login']);
//     }, 2000);
//   }

//   get f() {
//     return this.registrationForm.controls;
//   }
// }

import { Component, ViewChild, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ServicesService } from '../core/services/services.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  registrationForm: FormGroup;
  isLoading = false;
  errorMessage: string | null = null;
  successMessage: string | null = null;
  lockIconClass = 'fa-solid fa-lock';
  showPassword = false;
  showConfirmPassword = false;
  selectedFile: File | null = null; ///
  previewUrl: string | ArrayBuffer | null = null;
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private servicesService: ServicesService
  ) {
    this.registrationForm = this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: [''],
      username: ['', [Validators.required]],
      email: ['', [Validators.email]],
      phoneNo: ['', [Validators.pattern(/^[6-9]\d{9}$/)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
      profilePictureUrl: [''],
      about: [''],
      location: [''],
    }, { validator: this.passwordMatchValidator });
  }

  triggerFileInput(): void {
    this.fileInput.nativeElement.click();
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedFile = input.files[0];

      const reader = new FileReader();
      reader.onload = () => {
        this.previewUrl = reader.result;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  // Custom validator for password matching
  private passwordMatchValidator(form: FormGroup) {
    return form.get('password')?.value === form.get('confirmPassword')?.value
      ? null
      : { mismatch: true };
  }

  onSubmit() {
    if (this.registrationForm.invalid) {
      this.registrationForm.markAllAsTouched();
      return;
    }

    const formValues = this.registrationForm.value;

    // Ensure at least one of email or phoneNo is provided
    if (!formValues.email && !formValues.phoneNo) {
      this.errorMessage = 'Please provide either Email or Phone Number.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = null;
    this.successMessage = null;

    const userPayload = {
      firstName: formValues.firstName,
      lastName: formValues.lastName || null,
      username: formValues.username,
      password: formValues.password,
      email: formValues.email || null,
      phoneNo: formValues.phoneNo || null,
      profilePictureUrl: formValues.profilePictureUrl || null,
      about: formValues.about || null,
      location: formValues.location || null,
    };

    //   this.servicesService.register(userPayload).subscribe({
    //     next: (response) => {
    //       this.isLoading = false;
    //       this.successMessage = 'Registration successful! Redirecting to login...';
    //       setTimeout(() => {
    //         this.router.navigate(['/login']);
    //       }, 2000);
    //     },
    //     error: (error: HttpErrorResponse) => {
    //       this.isLoading = false;
    //       if (error.status === 400 || error.status === 409) {
    //         this.errorMessage = error.error?.message || 'Registration failed: Bad Request';
    //       } else if (error.status === 0) {
    //         this.errorMessage = 'Network error: Please check your connection';
    //       } else {
    //         this.errorMessage = 'Registration failed. Please try again.';
    //       }
    //     }
    //   });
    // }

    // Step 1: Register user
    this.servicesService.register(userPayload).subscribe({
      next: () => {
        // Step 2: Upload profile picture if selected
        if (this.selectedFile) {
          this.servicesService.uploadProfilePicture(this.selectedFile).subscribe({
            next: () => {
              this.successMessage = 'Registration complete with profile picture!';
              this.navigateToLogin();
            },
            error: () => {
              this.successMessage = 'User registered. Failed to upload picture.';
              this.navigateToLogin();
            }
          });
        } else {
          this.successMessage = 'Registration successful!';
          this.navigateToLogin();
        }
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading = false;
        if (error.status === 400 || error.status === 409) {
          this.errorMessage = error.error?.message || 'Registration failed: Bad Request';
        } else if (error.status === 0) {
          this.errorMessage = 'Network error: Please check your connection';
        } else {
          this.errorMessage = 'Registration failed. Please try again.';
        }
      }
    });
  }

  private navigateToLogin(): void {
    this.isLoading = false;
    setTimeout(() => {
      this.router.navigate(['/login']);
    }, 2000);
  }

  get f() {
    return this.registrationForm.controls;
  }
}