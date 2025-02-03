import { FeedItem } from "./feedItem.type";
import { User } from "./User.type";

export  interface Profile {
    user: User;
    postResponses: Array<FeedItem>;    
  }
