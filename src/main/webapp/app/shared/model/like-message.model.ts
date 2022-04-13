import { IMessage } from 'app/shared/model/message.model';

export interface ILikeMessage {
  id?: number;
  like?: boolean | null;
  users?: IMessage[] | null;
}

export const defaultValue: Readonly<ILikeMessage> = {
  like: false,
};
