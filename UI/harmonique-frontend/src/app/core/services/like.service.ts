import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class LikeService {

  private baseUrl = environment.likeApi;

  constructor(private http: HttpClient) { }

  likeSong(userId: number, songId: number): Observable<any> {
    const params = new HttpParams()
      .set('userId', userId.toString())
      .set('songId', songId.toString());
    return this.http.post(`${this.baseUrl}`, null, { params });
  }

  unlikeSong(userId: number, songId: number): Observable<any> {
    const params = new HttpParams().set('userId', userId.toString());
    return this.http.delete(`${this.baseUrl}/${songId}`, { params });
  }

  getLikedSongsByUser(userId: number): Observable<number[]> {
    return this.http.get<number[]>(`${this.baseUrl}/user/${userId}`);
  }

  getLikeCountForSong(songId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/song/${songId}`);
  }
}