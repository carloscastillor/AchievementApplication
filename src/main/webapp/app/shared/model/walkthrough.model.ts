import { IResource } from 'app/shared/model/resource.model';
import { IAchievement } from 'app/shared/model/achievement.model';

export interface IWalkthrough {
  id?: number;
  title?: string | null;
  description?: string | null;
  resource?: IResource | null;
  user?: IAchievement | null;
}

export const defaultValue: Readonly<IWalkthrough> = {};
