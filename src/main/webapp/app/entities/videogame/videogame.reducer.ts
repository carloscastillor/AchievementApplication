import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IVideogame, defaultValue } from 'app/shared/model/videogame.model';

const initialState: EntityState<IVideogame> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/videogames';

// Actions

export const getEntities = createAsyncThunk('videogame/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?cacheBuster=${new Date().getTime()}`;
  return axios.get<IVideogame[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'videogame/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IVideogame>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'videogame/create_entity',
  async (entity: IVideogame, thunkAPI) => {
    const result = await axios.post<IVideogame>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'videogame/update_entity',
  async (entity: IVideogame, thunkAPI) => {
    const result = await axios.put<IVideogame>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'videogame/partial_update_entity',
  async (entity: IVideogame, thunkAPI) => {
    const result = await axios.patch<IVideogame>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'videogame/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IVideogame>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const VideogameSlice = createEntitySlice({
  name: 'videogame',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = VideogameSlice.actions;

// Reducer
export default VideogameSlice.reducer;
