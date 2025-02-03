import { Component, OnInit, Input } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { AddPostDialogComponent } from '../add-post-dialog/add-post-dialog.component';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [RouterModule, MatDialogModule],
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css'
})
export class NavComponent implements OnInit {
  // input coming from parent component
  @Input() userId!: string;
  title = 'Instagram';

  constructor(private router: Router, private dialog: MatDialog) { }
  
  ngOnInit(): void {
    console.log('NavComponent initialized');
  }

  openAddPostDialog(): void {
    const dialogRef = this.dialog.open(AddPostDialogComponent, {
      width: '600px',
      data: { userId: this.userId }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(result);
      console.log('The dialog was closed');
      // Trigger a reload or refresh of the component
      window.location.reload();
    });
  }
}
