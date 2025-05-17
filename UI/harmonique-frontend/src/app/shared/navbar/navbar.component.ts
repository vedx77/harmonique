import { Component, HostListener, Inject, PLATFORM_ID, ViewChild } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../core/services/user.service';
import { environment } from '../../../environments/environment.development';
import { SearchService } from '../../core/services/search.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})

export class NavbarComponent {
  user: any = {};
  searchResults: any[] = [];

  // @ViewChild('menu') menu!: Menu;
  menuItems = [
    { label: 'Profile', icon: 'bi bi-person-circle', },
    { label: 'Settings', icon: 'bi bi-gear' },
    { label: 'Logout', icon: 'bi bi-box-arrow-right' }
  ];

  constructor(
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object,
    private userService: UserService,
    private searchService: SearchService
  ) { }

  // Search-related property
  searchQuery: string = ''; // Binds to input field
  userName: string = '';
  profilePictureUrl: string = '';

  // User menu logic (stub)
  toggleMenu(event: MouseEvent): void {
    event.stopPropagation();
    // TODO: Implement dropdown toggle or menu open logic here
    console.log('Toggle user menu clicked');
  }

  // Called when user presses Enter or triggers search
  handleSearch(): void {
    if (!this.searchQuery.trim()) return;

    this.searchService.searchSongs(this.searchQuery).subscribe({
      next: (results) => {
        this.searchResults = results;
        console.log('Search results:', results);

        // OPTIONAL: Navigate to a dedicated results page with query param
        this.router.navigate(['/search'], { queryParams: { q: this.searchQuery } });
      },
      error: (err) => {
        console.error('Search error:', err);
      }
    });
  }

  logOut() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('user');
      localStorage.removeItem('token');
    }
    this.router.navigate(['/login']);
    console.log("Logout successful, localStorage cleared.");
  }
  dropdownOpen = false;

  toggleDropdown(event: Event): void {
    event.stopPropagation();
    this.dropdownOpen = !this.dropdownOpen;
  }

  // Close dropdown when clicking outside
  @HostListener('document:click')
  closeDropdown(): void {
    this.dropdownOpen = false;
  }

  ngOnInit(): void {
    this.userService.getUserProfile().subscribe({
      next: (response) => {
        this.user = response;
        this.userName = response.name;
        this.profilePictureUrl = this.getProfileImageUrl(response.profilePictureUrl);
      },
      error: (error) => {
        console.error('Failed to fetch user profile', error);
      }
    });
  }

  getProfileImageUrl(fileName: string): string {
    return `${environment.uploadsBaseUrl}${fileName}`;
  }

  onImageError(event: Event) {
    (event.target as HTMLImageElement).src = 'assets/default-profile.png';
  }
}