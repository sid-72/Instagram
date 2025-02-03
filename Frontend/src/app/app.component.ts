import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { UserFeedComponent } from './user-feed/user-feed.component'; // Adjust the path as necessary
declare var $: any;

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Instagram';

  ngOnInit() {
    $(document).ready(function() {
      console.log('jQuery is ready!');
    });
  }
}
