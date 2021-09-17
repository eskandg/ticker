import React from 'react';
import { createAction, createSlice, isFulfilled, isPending } from '@reduxjs/toolkit';
import { log } from 'react-jhipster';

const initialState = {
  loading: false,
  errorMessage: null,
  isSocketOpen: false,
  subscriptions: [],
  updating: false,
  updateSuccess: false,
};

export const setIsSocketOpen = createAction<boolean>('socket/set_is_open');
export const addSubscriptions = createAction<Array<string>>('socket/add_subscriptions');

export const WebSocketSlice = createSlice({
  name: 'socket',
  initialState,
  reducers: {
    set_is_open(state, action) {
      state.isSocketOpen = action.payload;
    },
    add_subscriptions(state, action) {
      state.subscriptions = [...new Set([...state.subscriptions, ...action.payload])];
    },
  },
});

export default WebSocketSlice.reducer;
