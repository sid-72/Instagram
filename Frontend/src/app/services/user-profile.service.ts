import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Profile } from '../model/Profile.type';
@Injectable({
  providedIn: 'root'
})
export class UserProfileService {


http = inject(HttpClient);
  constructor(){}



  getFeed(userId: string): Observable<Profile> {
    const feedUrl = `https://instagram-tl17.onrender.com/users/${userId}/details`; 
    return this.http.get<Profile>(feedUrl);
  }

}
