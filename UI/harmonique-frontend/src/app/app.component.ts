import { Component, OnDestroy } from '@angular/core';
import { RouterOutlet, RouterModule, NavigationEnd, Router } from '@angular/router';
import { SidebarComponent } from './shared/sidebar/sidebar.component';
import { NgIf, NgClass } from '@angular/common';
import { FooterComponent } from './shared/footer/footer.component';
import { HttpClientModule } from '@angular/common/http';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { Subscription, filter } from 'rxjs';

const NO_LAYOUT_ROUTES = ['/login', '/register'];
const HIDE_FOOTER_ROUTES = ['/login', '/register'];
const HIDE_NAVBAR_ROUTES = ['/login', '/register'];

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterModule,
    SidebarComponent,
    NavbarComponent,
    FooterComponent,
    HttpClientModule,
    NgIf,
    NgClass
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnDestroy {
  title = 'harmonique';
  showNavAndSidebar = true;
  showFooter = true;
  showNavbar = true;
  layoutClass = 'with-layout';
  showLayout = true;

  private routerSubscription: Subscription;

  constructor(private router: Router) {
    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event) => {
        if (event instanceof NavigationEnd) {
          const currentUrl = event.url.split('?')[0]; // Remove query params

          this.showLayout = !NO_LAYOUT_ROUTES.includes(currentUrl);
          this.showNavAndSidebar = !NO_LAYOUT_ROUTES.includes(currentUrl);
          this.showFooter = !HIDE_FOOTER_ROUTES.includes(currentUrl);
          this.showNavbar = !HIDE_NAVBAR_ROUTES.includes(currentUrl);
          this.layoutClass = NO_LAYOUT_ROUTES.includes(currentUrl) ? 'no-layout' : 'with-layout';
        }
      });
  }

  ngOnDestroy(): void {
    this.routerSubscription?.unsubscribe();
  }
}