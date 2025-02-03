import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PostService } from '../services/post.service';

@Component({
  selector: 'app-add-post-dialog',
  templateUrl: './add-post-dialog.component.html',
  styleUrls: ['./add-post-dialog.component.css']
})
export class AddPostDialogComponent implements OnInit {
  postTitle: string = '';
  postContent: string = '';
  postImage: File | null = null;
  userId: string = '';
  constructor(
    public dialogRef: MatDialogRef<AddPostDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public postService: PostService
  ) {}
    ngOnInit(): void {
    this.userId = this.data.userId;
    }
  


  onFileSelected(event: any): void {
    this.postImage = event.target.files[0];
  }

  onCaptionInput(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    this.postContent = inputElement.value;
  }
 
  onSavePost(): void {
    // Handle the save post logic here
    console.log('Post saved:', this.userId, this.postContent, this.postImage);
    // You can send the data to a service or handle it as needed
    if (this.postImage) {
      this.postService.sendPost(this.userId, this.postImage, this.postContent).subscribe(response => {
        console.log('Post saved successfully:', response);
        this.dialogRef.close({ title: this.postTitle, content: this.postContent, image: this.postImage });
      }, error => {
        console.error('Failed to save post:', error);
      });

    } else {
      console.error('Post image is required and cannot be null.');
    }
   
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}