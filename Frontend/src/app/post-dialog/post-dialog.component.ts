import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { FeedItem } from '../model/feedItem.type';
import { PostComponent } from '../post/post.component';

@Component({
  selector: 'app-post-dialog',
  standalone: true,
  imports: [CommonModule, PostComponent, MatDialogModule],
  templateUrl: './post-dialog.component.html',
  styleUrl: './post-dialog.component.css'
})
export class PostDialogComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<PostDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit(): void {
    console.log('PostDialogComponent initialized');
    this.data.post.user = this.data.user;
    console.log('Post data:', this.data);
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

}
