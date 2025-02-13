import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PostRequest } from '../model/PostRequest.type';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  http = inject(HttpClient);

  constructor() { }

  sendPost(userId: string, file: File, caption: string): Observable<HttpResponse<string>> {
        var feedUrl = `https://instagram-tl17.onrender.com/user/${userId}/post`; 
        const headers = new HttpHeaders(); // No need to set 'Content-Type' for FormData
        // make post request to the server
        //Comment comment = new Comment();
        const postRequest: PostRequest = {
          file: file,
          caption: caption,
        };
        console.log(postRequest);
        const formData: FormData = new FormData();
        formData.append('file', file);
        formData.append('caption', caption);

        return this.http.post(feedUrl, formData, {headers, responseType: 'text',  observe: 'response' });
      }
}
