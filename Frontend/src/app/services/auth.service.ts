// filepath: /c:/Users/siddh/OneDrive/Desktop/Angular/Instagram/src/app/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/users/login'; // Replace with your API endpoint

  constructor(private http: HttpClient) { }

  login(data: any): Observable<HttpResponse<string>> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(this.apiUrl, JSON.stringify(data), { headers, responseType: 'text', observe: 'response' });
  }
}