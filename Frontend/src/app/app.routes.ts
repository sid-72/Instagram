import { Routes } from '@angular/router';
import { UserFeedComponent } from './user-feed/user-feed.component';
import { LoginComponent } from './login/login.component';
import { UserProfileComponent } from './user-profile/user-profile.component';

export const routes: Routes = [
  { path: 'feed/user/:userId', component: UserFeedComponent },
  { path: 'login', component: LoginComponent },
  //{ path: '', redirectTo: '/feed', pathMatch: 'full' }, // Default route
  //{ path: '**', redirectTo: '/feed' }, // Wildcard route for a 404 page
  {path: 'users/:userId/profile', component: UserProfileComponent}  
];
