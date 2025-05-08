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
      name: 'Alex Johnson',
      role: 'Founder & Lead Developer',
      bio: 'Music enthusiast with 10+ years of experience in audio engineering and software development.',
      avatar: 'assets/images/team/alex.jpg'
    },
    {
      name: 'Maria Chen',
      role: 'UI/UX Designer',
      bio: 'Specializes in creating intuitive music interfaces that delight users.',
      avatar: 'assets/images/team/maria.jpg'
    },
    {
      name: 'Jamal Williams',
      role: 'Community Manager',
      bio: 'Connects local artists with audiences and builds our music community.',
      avatar: 'assets/images/team/jamal.jpg'
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