import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-settings',
  imports: [CommonModule, FormsModule,RouterModule],
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent {
  settings = {
    username: '',
    email: '',
    autoplay: false,
    shuffle: false,
    theme: 'dark'
  };

  saveSettings(): void {
    console.log('Settings saved:', this.settings);
    // Implement further logic to persist settings
    alert('Settings saved successfully!');
  }
  goBack(): void {
    window.history.back();
  }
}
