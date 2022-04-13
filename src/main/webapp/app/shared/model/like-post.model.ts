import { IPost } from 'app/shared/model/post.model';

export interface ILikePost {
  id?: number;
  like?: boolean | null;
  users?: IPost[] | null;
}

export const defaultValue: Readonly<ILikePost> = {
  like: false,
};
