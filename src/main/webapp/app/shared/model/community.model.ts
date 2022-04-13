import { IPost } from 'app/shared/model/post.model';

export interface ICommunity {
  id?: number;
  name?: string | null;
  post?: IPost | null;
}

export const defaultValue: Readonly<ICommunity> = {};
