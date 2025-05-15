import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  private loggedIn = new BehaviorSubject<boolean>(false);
  private token: string | null = null;

  constructor(private router: Router) {
    if (typeof window !== 'undefined' && window.localStorage) {
      this.token = localStorage.getItem('token');
    }
    if (this.token) {
      this.loggedIn.next(true);
    }
  }

  storeToken(token: string) {
    if (typeof window !== 'undefined') {
      localStorage.setItem('token', token);
      this.token = token;
      this.loggedIn.next(true);
    }
  }

  getToken() {
    return this.token;
  }

  logout() {
    localStorage.removeItem('token');
    this.clearUserData();
    this.token = null;
    this.loggedIn.next(false);
    this.router.navigate(['/login']);
  }

  isLoggedIn() {
    return this.loggedIn.asObservable();
  }

  storeUserData(user: any) {
    if (typeof window !== 'undefined') {
      localStorage.setItem('userData', JSON.stringify(user));
    }
  }

  getUserData() {
    if (typeof window !== 'undefined') {
      const data = localStorage.getItem('userData');
      return data ? JSON.parse(data) : null;
    }
    return null;
  }

  clearUserData() {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('userData');
    }
  }
}