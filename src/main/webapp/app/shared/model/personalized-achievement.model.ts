import { IAchievement } from 'app/shared/model/achievement.model';
import { IVideogame } from 'app/shared/model/videogame.model';

export interface IPersonalizedAchievement {
  id?: number;
  achievement?: IAchievement | null;
  users?: IVideogame[] | null;
}

export const defaultValue: Readonly<IPersonalizedAchievement> = {};
