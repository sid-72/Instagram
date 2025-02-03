import { Component, OnInit, signal } from '@angular/core';
import { FeedServiceService } from '../services/feed-service.service';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FeedItem } from '../model/feedItem.type';
import { NavComponent } from "../nav/nav.component";
import { CommentService } from '../services/comment.service';

@Component({
  selector: 'app-user-feed',
  standalone: true,
  imports: [CommonModule, NavComponent],
  templateUrl: './user-feed.component.html',
  styleUrls: ['./user-feed.component.css']
})
export class UserFeedComponent implements OnInit {
  constructor(private feedService: FeedServiceService, private route: ActivatedRoute,
    private commentService: CommentService) { }

  feedItems = signal<Array<FeedItem>>([]);
  userId = signal<string>('');
  public showCommentInput = false;

  ngOnInit(): void {
    this.userId.set( this.route.snapshot.paramMap.get('userId') || '');


    this.feedService.getFeed(this.userId()).subscribe(data => {
      this.feedItems.set(data);
      console.log(data);
    });
  }


  toggleCommentInput(): void {
    this.showCommentInput = !this.showCommentInput;
  }

  postComment(userId: string, postId: string, commentString: string, item: FeedItem): void {
    commentString = commentString.trim();
    if (commentString) {
      this.commentService.postComment(this.userId(), postId, commentString).subscribe(response => {
        if (response.status === 200) {
          item.comments = item.comments || [];
          item.comments.push({ userId: this.userId(), content: commentString, postId:  postId, id: '0' });
          this.showCommentInput = false;
        }
      });
    }
  }


  
}


