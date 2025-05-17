// src/app/home/services/search.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private baseUrl = `${environment.songsApi}/search`; // Adjust as per environment

  constructor(private http: HttpClient) {}

  searchSongs(query: string): Observable<any[]> {
    const params = new HttpParams().set('query', query);
    return this.http.get<any[]>(this.baseUrl, { params });
  }
}