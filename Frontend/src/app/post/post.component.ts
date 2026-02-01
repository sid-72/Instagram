import { Component, Input, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FeedItem } from '../model/feedItem.type';
import { CommentService } from '../services/comment.service';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent {
  @Input() item!: FeedItem;
  @Input() currentUserId!: string;

  public showCommentInput = signal<boolean>(false);

  constructor(private commentService: CommentService) {}

  toggleCommentInput(): void {
    this.showCommentInput.set(!this.showCommentInput());
  }

  postComment(commentString: string): void {
    commentString = commentString.trim();
    if (commentString) {
      this.commentService.postComment(this.currentUserId, this.item.postId, commentString).subscribe(response => {
        if (response.status === 200) {
          this.item.comments = this.item.comments || [];
          this.item.comments.push({ userId: this.currentUserId, content: commentString, postId: this.item.postId, id: '0' });
          this.showCommentInput.set(false);
        }
      });
    }
  }
}
