import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { environment } from '../../../environments/environment.development';
import { AuthService } from '../../core/services/auth.service';
import { UserService } from '../../core/services/user.service';
import { ServicesService } from '../../core/services/services.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
  imports: [CommonModule, FormsModule]
})
export class ProfileComponent implements OnInit {
  user: any = {};
  selectedFile: File | null = null;
  uploadsBaseUrl = environment.uploadsBaseUrl;
  public environment = environment;

  constructor(
    private location: Location,
    private authService: AuthService,
    private userService: UserService,
    private servicesService: ServicesService
  ) { }

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    this.userService.getUserProfile().subscribe({
      next: (res) => {
        this.user = res;
      },
      error: (err) => {
        console.error('Failed to load user profile:', err);
      }
    });
  }

  // Handle avatar file selection
  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = () => {
        this.user.profilePictureUrl = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  triggerFileUpload(): void {
    const input = document.getElementById('avatarUpload') as HTMLElement;
    input.click();
  }

  onSubmit(): void {
    const updatedData: any = {
      firstName: this.user.firstName,
      lastName: this.user.lastName,
      username: this.user.username,
      email: this.user.email,
      phoneNo: this.user.phoneNo,
      about: this.user.about,
      location: this.user.location,
    };

    if (this.selectedFile) {
      updatedData.profilePicture = this.selectedFile;
    }

    this.servicesService.updateProfile(updatedData).subscribe({
      next: (res) => {
        alert('Profile updated successfully!');
        this.loadUserProfile();
      },
      error: (err) => {
        console.error('Update failed:', err);
        alert('Failed to update profile.');
      }
    });
  }

  cancelEdit(): void {
    this.goBack();
  }

  goBack(): void {
    this.location.back();
  }
}