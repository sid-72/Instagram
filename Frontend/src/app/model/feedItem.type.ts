import { Comment } from "./Comment.type";
import { User } from "./User.type";



export interface FeedItem {
  postId: string;
  user: User;
  caption: string;
  image_path: string;
  comments?: Comment[];
}