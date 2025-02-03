import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { FeedItem } from '../model/feedItem.type';
@Injectable({
  providedIn: 'root'
})
export class FeedServiceService {
  http = inject(HttpClient);
  constructor(){}



  getFeed(userId: string): Observable<Array<FeedItem>> {
    var feedUrl = `http://localhost:8080/feed/{userId}`; 
    return this.http.get<Array<FeedItem>>(feedUrl);
  }
}
