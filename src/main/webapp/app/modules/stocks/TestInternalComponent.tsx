import React, { useState } from 'react';
import { useAppSelector, useAppDispatch } from 'app/config/store';
import { getEntity } from '../../entities/ticker/ticker.reducer';

export const TestInternalComponoent = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const ticker = useAppSelector(state => state.ticker.entity);

  return <h1>test {ticker.symbol}</h1>;
};

export default TestInternalComponoent;
