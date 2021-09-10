import React, { useEffect, useState } from 'react';
import { useAppSelector, useAppDispatch } from 'app/config/store';
import { getStock, getStocks, updateStocksLive } from 'app/modules/stocks/stocks-reducer';
import { getEntity as getProfile } from 'app/entities/ticker-profile/ticker-profile.reducer';
import { RouteComponentProps, useParams } from 'react-router-dom';
import { getUrlParameter, log } from 'react-jhipster';
import { stringify } from 'ts-jest/dist/utils/json';

export const Profile = (props: RouteComponentProps<{ symbol: string }>) => {
  const dispatch = useAppDispatch();
  const urlParams = useParams();
  const tickerData = useAppSelector(state => state.ticker.entity);
  const profile = useAppSelector(state => state.tickerProfile.entity);

  useEffect(() => {
    dispatch(getStock({ symbol: urlParams['symbol'], getMoreDetails: true, isSocketActive: false }));
    dispatch(getProfile(urlParams['symbol']));
  }, []);

  return <p>{tickerData && JSON.stringify(tickerData)}</p>;
};

export default Profile;
