import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-about-us',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './about-us.component.html',
  styleUrls: ['./about-us.component.scss']
})
export class AboutUsComponent {
  goBack(): void {
    window.history.back();
  }
  teamMembers = [
    {
      name: 'Vedant Shinde',
      role: 'Full Stack Developer',
      bio: 'Music enthusiast with an experience of 10+ years of listening to music and 3+ years of software development.',
      avatar: 'assets/images/team/Employee.webp'
    }
  ];

  features = [
    {
      icon: '🎵',
      title: 'Local Music Discovery',
      description: 'Find hidden musical gems in your neighborhood'
    },
    {
      icon: '🎤',
      title: 'Artist Support',
      description: 'Directly support independent local artists'
    },
    {
      icon: '🤝',
      title: 'Community Driven',
      description: 'Powered by and for local music lovers'
    },
    {
      icon: '🔊',
      title: 'High Quality Audio',
      description: 'Studio-grade streaming quality'
    }
  ];
}