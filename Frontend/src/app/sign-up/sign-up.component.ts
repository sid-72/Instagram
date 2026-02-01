import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html'
})
export class SignUpComponent implements OnInit {
  title = 'Instagram';
  private apiUrl = 'https://instagram-backend-d129.onrender.com/users';

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit() {
    console.log('Sign up component is ready!');
  }

  signUp(name: string, userId: string, password: string): Observable<HttpResponse<string>> {
    const formData = new FormData();
    formData.append('name', name);
    formData.append('userId', userId);
    formData.append('password', password);
    
    console.log('Sign up formData:', { name, userId, password });
    
    // Don't set Content-Type header - browser sets it automatically for FormData
    return this.http.post(this.apiUrl, formData, { responseType: 'text', observe: 'response' });
  }

  onSubmit(event: Event) {
    event.preventDefault();
    const form = event.target as HTMLFormElement;
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());
    const name = data['name'] as string;
    const userId = data['userId'] as string;
    const password = data['password'] as string;

    console.log('Sign up data:', { name, userId, password });

    this.signUp(name, userId, password).subscribe((response: HttpResponse<string>) => {
      if (response.body == "user added") {
        console.log('Sign up successful', response.body);
        // Handle successful sign up
        // Navigate to login page or user feed
        this.router.navigate(['/login']);
      } else {
        console.log('Sign up failed', response.body);
      }
    }, (error: any) => {
      console.error('Sign up failed', error);
      // Handle sign up error
    });
  }
}
