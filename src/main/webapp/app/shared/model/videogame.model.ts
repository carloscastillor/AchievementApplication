import { IAchievement } from 'app/shared/model/achievement.model';
import { IPersonalizedAchievement } from 'app/shared/model/personalized-achievement.model';

export interface IVideogame {
  id?: number;
  name?: string | null;
  achievement?: IAchievement | null;
  personalizedAchievement?: IPersonalizedAchievement | null;
}

export const defaultValue: Readonly<IVideogame> = {};
