import { IVideogame } from 'app/shared/model/videogame.model';

export interface IAchievement {
  id?: number;
  name?: string | null;
  description?: string | null;
  videogame?: string | null;
  completed?: boolean | null;
  videogames?: IVideogame[] | null;
}

export const defaultValue: Readonly<IAchievement> = {
  completed: false,
};
