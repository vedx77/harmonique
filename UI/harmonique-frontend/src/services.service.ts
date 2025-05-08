import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from './environments/environment.development';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ServicesService {
  http = inject(HttpClient);

  login(data: { email: string, password: string }): Observable<any> {
    const url = environment.login + '/login';
    return this.http.post<any>(url, data);
  }

  register(data: { name: string, email: string, password: string }): Observable<any> {
    const url = environment.register + '/register';
    // Here, we manually add about: '' (empty string) for now
    return this.http.post<any>(url, { ...data, about: '' });
  }
}