import { Component } from '@angular/core';
import { UserProfileService } from '../services/user-profile.service';
import { FeedItem } from '../model/feedItem.type';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { PostDialogComponent } from '../post-dialog/post-dialog.component';
import { CommonModule } from '@angular/common';
import { NavComponent } from '../nav/nav.component';

@Component({
  selector: 'app-user-profile',
  imports: [CommonModule, NavComponent],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent {
  posts : FeedItem[] = [];
  userId: string = '';
  name : string = '';
  constructor(
    private UserProfileService: UserProfileService,
    private dialog: MatDialog,
    private route: ActivatedRoute
  ) { } 

  ngOnInit(): void {
    this.userId = this.route.snapshot.paramMap.get('userId') || '';
    console.log('UserProfileComponent initialized');
    this.getUserProfile(this.userId);
  }

  // call the service to get the user profile
  getUserProfile(userId: string): void {
    // call the service to get the user profile
    this.UserProfileService.getFeed(userId).subscribe(response => {
      this.posts = response.postResponses;
      this.userId = response.user.id;
      this.name = response.user.name;
      console.log('User profile:', response);
    }, error => {
      console.error('Failed to get user profile:', error);
    });
  }

  openPostDialog(post: FeedItem): void {
    this.dialog.open(PostDialogComponent, {
      width: '600px',
      height: '300vh',
      data:  {post: post, userId: this.userId} 
    });
  }

}
