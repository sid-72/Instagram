import { Injectable, inject } from '@angular/core';
import { combineLatest, Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Comment } from '../model/Comment.type';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
    http = inject(HttpClient);

  constructor() { }

  postComment(userId: string, postId: string, content: string): Observable<HttpResponse<string>> {
      var feedUrl = `https://instagram-tl17.onrender.com/comments`; 
      const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
      // make post request to the server
      //Comment comment = new Comment();
      const comment: Comment = {
        userId: userId,
        postId: postId,
        content: content,
        id: "1"
      };
      console.log(comment);
      return this.http.post(feedUrl, JSON.stringify(comment), { headers, responseType: 'text', observe: 'response' });
    }
  }

