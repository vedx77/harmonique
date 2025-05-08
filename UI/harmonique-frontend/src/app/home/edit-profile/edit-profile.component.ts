import { Component } from '@angular/core';
import { Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.scss'],
  imports: [CommonModule, FormsModule]
})
export class EditProfileComponent {
  // Default active tab
  activeTab: string = 'profile';

  // Form model for profile, privacy, and preferences
  profileForm = {
    firstName: 'Sarah',
    lastName: 'Johnson',
    username: 'sarahj',
    bio: 'Music enthusiast • Playlist curator • Local music supporter',
    location: 'New York, USA',
    joinDate: 'June 2021',
    privacySettings: {
      visibility: 'public', // 'public' | 'followers' | 'private'
      shareListening: true,
      sharePlaylists: true,
      shareFollowers: false
    },
    preferences: {
      autoplay: true,
      explicitContent: false,
      audioQuality: 'medium', // 'low' | 'medium' | 'high'
      theme: 'light',         // 'light' | 'dark' | 'system'
      accentColor: '#6e45e2'
    }
  };

  constructor(private location: Location) { }

  // Change active tab
  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }

  // Check if tab is currently active
  isTabActive(tab: string): boolean {
    return this.activeTab === tab;
  }

  // Navigate back
  goBack(): void {
    this.location.back();
  }

  // Handle avatar image upload
  onAvatarUpload(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const reader = new FileReader();
      reader.onload = (e) => {
        const avatar = document.getElementById('profileAvatar') as HTMLImageElement;
        if (avatar && e.target) {
          avatar.src = e.target.result as string;
        }
      };
      reader.readAsDataURL(input.files[0]);
    }
  }

  // Set selected privacy visibility
  setPrivacyOption(option: 'public' | 'followers' | 'private'): void {
    this.profileForm.privacySettings.visibility = option;
  }

  // Set selected accent color
  setAccentColor(color: string): void {
    this.profileForm.preferences.accentColor = color;
    console.log('Accent color changed to:', color);
  }

  // Save profile info
  saveProfile(): void {
    console.log('Profile saved:', this.profileForm);
  }

  // Save privacy settings
  savePrivacySettings(): void {
    console.log('Privacy settings saved:', this.profileForm.privacySettings);
  }

  // Save preferences
  savePreferences(): void {
    console.log('Preferences saved:', this.profileForm.preferences);
  }
}
