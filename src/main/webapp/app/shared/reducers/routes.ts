import { createAction, createSlice } from '@reduxjs/toolkit';
import { log } from 'react-jhipster';

export const getCurrentRoute = createAction<string>('routes/get_current_route');

export const RouteSlice = createSlice({
  name: 'routes',
  initialState: { location: '/' },
  reducers: {
    get_current_route(state, action) {
      state.location = action.payload;
    },
  },
});

export default RouteSlice.reducer;
