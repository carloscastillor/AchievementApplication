import { IMessage } from 'app/shared/model/message.model';
import { ILikePost } from 'app/shared/model/like-post.model';

export interface IPost {
  id?: number;
  title?: string | null;
  description?: string | null;
  message?: IMessage | null;
  likePost?: ILikePost | null;
}

export const defaultValue: Readonly<IPost> = {};
