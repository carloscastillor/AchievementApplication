import { ILikeMessage } from 'app/shared/model/like-message.model';

export interface IMessage {
  id?: number;
  body?: string | null;
  likeMessage?: ILikeMessage | null;
}

export const defaultValue: Readonly<IMessage> = {};
