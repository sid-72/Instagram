import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  title = 'Instagram';
  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
    console.log('Login component is ready!');
  }

  onSubmit(event: Event) {
    event.preventDefault();
    const form = event.target as HTMLFormElement;
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());
    console.log(data);

    this.authService.login(data).subscribe((response: HttpResponse<string>) => {
      console.log('Login successful', response.body);
      // Handle successful login
      // navigate to user feed
      if(response.body!="failed") {
      const userId = response.body; // Assuming response body contains userId
      this.router.navigate([`/feed/user/${userId}`]);
      }

    }, (error: any) => {
      console.error('Login failed', error);
      // Handle login error
    });
  }
}