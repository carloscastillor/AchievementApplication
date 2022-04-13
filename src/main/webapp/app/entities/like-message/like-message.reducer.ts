import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { ILikeMessage, defaultValue } from 'app/shared/model/like-message.model';

const initialState: EntityState<ILikeMessage> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/like-messages';

// Actions

export const getEntities = createAsyncThunk('likeMessage/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?cacheBuster=${new Date().getTime()}`;
  return axios.get<ILikeMessage[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'likeMessage/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<ILikeMessage>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'likeMessage/create_entity',
  async (entity: ILikeMessage, thunkAPI) => {
    const result = await axios.post<ILikeMessage>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'likeMessage/update_entity',
  async (entity: ILikeMessage, thunkAPI) => {
    const result = await axios.put<ILikeMessage>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'likeMessage/partial_update_entity',
  async (entity: ILikeMessage, thunkAPI) => {
    const result = await axios.patch<ILikeMessage>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'likeMessage/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<ILikeMessage>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const LikeMessageSlice = createEntitySlice({
  name: 'likeMessage',
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

export const { reset } = LikeMessageSlice.actions;

// Reducer
export default LikeMessageSlice.reducer;
