import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment.development';
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

  register(data: {
    firstName: string;
    lastName?: string;
    username: string;
    password: string;
    email?: string;
    phoneNo?: string;
    profilePicture?: File;
    about?: string;
    location?: string;
  }): Observable<any> {
    const url = environment.register + '/register';

    const formData = new FormData();
    formData.append('firstName', data.firstName);
    formData.append('lastName', data.lastName || '');
    formData.append('username', data.username);
    formData.append('password', data.password);
    formData.append('email', data.email || '');
    formData.append('phoneNo', data.phoneNo || '');
    formData.append('about', data.about || '');
    formData.append('location', data.location || '');

    if (data.profilePicture) {
      formData.append('file', data.profilePicture);
    }
    return this.http.post<any>(url, formData);
  }

  updateProfile(data: {
    firstName: string;
    lastName?: string;
    username: string;
    email?: string;
    phoneNo?: string;
    about?: string;
    location?: string;
    profilePicture?: File;
  }): Observable<any> {
    const url = environment.userBaseUrl + '/profile';

    const formData = new FormData();
    formData.append('firstName', data.firstName);
    formData.append('lastName', data.lastName || '');
    formData.append('username', data.username);
    formData.append('email', data.email || '');
    formData.append('phoneNo', data.phoneNo || '');
    formData.append('about', data.about || '');
    formData.append('location', data.location || '');

    if (data.profilePicture) {
      formData.append('file', data.profilePicture);
    }

    return this.http.put<any>(url, formData);
  }

  uploadProfilePicture(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    const url = environment.profilePictureUpload;
    return this.http.put<any>(url, formData);
  }
}