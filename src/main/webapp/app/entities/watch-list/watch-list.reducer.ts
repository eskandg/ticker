import axios from 'axios';
import { createAction, createAsyncThunk, createSlice, isFulfilled, isPending } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { defaultValue, IWatchList } from 'app/shared/model/watch-list.model';
import { log } from 'react-jhipster';

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [],
  userFollows: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/watch-lists';

export type IWatchListString = { user: string; tickerSymbol: string };

// Actions

export const getEntities = createAsyncThunk('watchList/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?cacheBuster=${new Date().getTime()}`;
  return axios.get<IWatchList[]>(requestUrl);
});

export const getEntitiesByUser = createAsyncThunk(
  'watchList/fetch_entity_list_by_user',
  async (user: string) => {
    const requestUrl = `${apiUrl}/${user}`;
    return axios.get<IWatchList[]>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const getEntity = createAsyncThunk(
  'watchList/fetch_entity',
  async ({ user, tickerSymbol }: IWatchListString) => {
    const requestUrl = `${apiUrl}/${user}/${tickerSymbol}`;
    return axios.get<IWatchList>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'watchList/create_entity',
  async (entity: IWatchList, thunkAPI) => {
    return await axios.post<IWatchList>(apiUrl, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'watchList/update_entity',
  async (entity: IWatchList, thunkAPI) => {
    const result = await axios.put<IWatchList>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'watchList/partial_update_entity',
  async (entity: IWatchList, thunkAPI) => {
    const result = await axios.patch<IWatchList>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'watchList/delete_entity',
  async ({ user, tickerSymbol }: IWatchListString, thunkAPI) => {
    const requestUrl = `${apiUrl}/${user}/${tickerSymbol}`;
    return await axios.delete<IWatchList>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const setUserFollows = createAction<object>('watchList/set_user_follows');
export const clearUserWatchListState = createAction('watchList/clear_watchlist');

// slice

export const WatchListSlice = createSlice({
  name: 'watchList',
  initialState,
  reducers: {
    set_user_follows(state, action) {
      let index;
      if (action.payload.type === 'remove' && new Set(state.userFollows).has(action.payload.tickerSymbol)) {
        index = state.userFollows.indexOf(action.payload.tickerSymbol);
        state.userFollows.splice(index, 1);
      } else state.userFollows = [...new Set([...state.userFollows, action.payload.tickerSymbol])];
    },
    clear_watchlist(state, action) {
      state.entity = defaultValue;
      state.entities = [];
      state.userFollows = [];
    },
  },
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
        return {
          ...state,
          loading: false,
          entities: action.payload.data,
        };
      })
      .addMatcher(isFulfilled(getEntitiesByUser), (state, action) => {
        let symbols = [];
        action.payload.data.map(entity => symbols.push(entity['tickerSymbol']));
        return {
          ...state,
          loading: false,
          entities: action.payload.data,
          userFollows: symbols,
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntitiesByUser, getEntity), state => {
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

export const { reset } = WatchListSlice.actions;

// Reducer
export default WatchListSlice.reducer;
