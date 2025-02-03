import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FeedItem } from '../model/feedItem.type';
import { CommentService } from '../services/comment.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-post-dialog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './post-dialog.component.html',
  styleUrl: './post-dialog.component.css'
})
export class PostDialogComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<PostDialogComponent>,
    public commentService: CommentService,
    @Inject(MAT_DIALOG_DATA) public data: { post: FeedItem, userId: string}
  ) { }

  ngOnInit(): void {
    console.log('PostDialogComponent initialized');
    console.log('Post data:', this.data);

  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  addComment(newComment: string, postId: string): void {
    if (newComment.trim()) {
      this.commentService.postComment(this.data.userId, this.data.post.postId, newComment).subscribe(response => {
        if (response.status === 200) {
          if (this.data.post.comments) {
            this.data.post.comments.push({ userId: this.data.userId, content: newComment, id: '', postId: postId });
          }
        }
      });
    }
  }

}
