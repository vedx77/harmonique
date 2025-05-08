import {
  Component, ViewEncapsulation,
  OnInit,
  ChangeDetectionStrategy,
  Output,
  EventEmitter
} from '@angular/core';

import {
  Router,
  RouterLink,
  RouterModule,
  NavigationEnd
} from '@angular/router';

import {
  CommonModule,
  NgIf
} from '@angular/common';

interface MenuItem {
  id: number;
  label: string;
  icon: string;
  route: string;
  exact?: boolean;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, NgIf, RouterLink, RouterModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  encapsulation: ViewEncapsulation.None
})

export class SidebarComponent implements OnInit {
  @Output() sidebarState = new EventEmitter<boolean>();

  isExpanded = false;
  selectedItem: number | null = null;
  hoverTimeout: any;

  menuItems: MenuItem[] = [
    { id: 1, label: 'Home', icon: 'assets/Dashboard.png', route: '/home', exact: true },
    { id: 2, label: 'Liked Songs', icon: 'assets/heart.png', route: '/liked-songs' },
    { id: 3, label: 'About Us', icon: 'assets/chat.png', route: '/about-us' },
    { id: 4, label: 'FAQs', icon: 'assets/question.png', route: '/faq' },
    { id: 5, label: 'Settings', icon: 'assets/gear.png', route: '/settings' }
  ];

  constructor(private router: Router,) { }

  ngOnInit(): void {
    this.setActiveItem();
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.setActiveItem();
      }
    });
  }

  private setActiveItem(): void {
    const activeItem = this.menuItems.find(item =>
      item.exact
        ? this.router.url === item.route
        : this.router.url.startsWith(item.route)
    );
    this.selectedItem = activeItem?.id || null;
  }

  expandSidebar() {
    clearTimeout(this.hoverTimeout);
    this.isExpanded = true;
    this.sidebarState.emit(true);
  }

  collapseSidebar() {
    this.hoverTimeout = setTimeout(() => {
      if (!this.isExpanded) return;
      this.isExpanded = false;
      this.sidebarState.emit(false);
    }, 300); // Small delay to prevent flickering
  }

  cancelCollapse() {
    clearTimeout(this.hoverTimeout);
  }

  toggleSidebar(): void {
    this.isExpanded = !this.isExpanded;
    this.sidebarState.emit(this.isExpanded);
  }

  selectItem(item: MenuItem): void {
    if (this.router.url !== item.route) {
      this.router.navigate([item.route]);
    }
    // Auto-collapse for mobile if needed
    if (window.innerWidth < 768) {
      this.isExpanded = false;
      this.sidebarState.emit(false);
    }
  }

  trackByItemId(index: number, item: MenuItem): number {
    return item.id;
  }
}