import { Component, OnInit } from '@angular/core';
import { FeedServiceService } from '../services/feed-service.service';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FeedItem } from '../model/feedItem.type';
import { NavComponent } from "../nav/nav.component";
import { PostComponent } from '../post/post.component';

@Component({
  selector: 'app-user-feed',
  standalone: true,
  imports: [CommonModule, NavComponent, PostComponent],
  templateUrl: './user-feed.component.html',
  styleUrls: ['./user-feed.component.css']
})
export class UserFeedComponent implements OnInit {
  constructor(private feedService: FeedServiceService, private route: ActivatedRoute) { }

  feedItems: Array<FeedItem> = [];
  userId: string = '';

  ngOnInit(): void {
    this.userId = this.route.snapshot.paramMap.get('userId') || '';


    this.feedService.getFeed(this.userId).subscribe(data => {
      this.feedItems = data.map(item => ({
        ...item,
        imageLoaded: false
      }));
    });

  }

}


