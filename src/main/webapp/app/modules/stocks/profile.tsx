import React, { useEffect, useState } from 'react';
import { useAppSelector, useAppDispatch } from 'app/config/store';
import { addSymbol, getChart, getStock, getStocks, updateStocksLive } from 'app/modules/stocks/stocks-reducer';
import { getEntity as getProfile } from 'app/entities/ticker-profile/ticker-profile.reducer';
import { RouteComponentProps, useParams } from 'react-router-dom';
import { getUrlParameter, log } from 'react-jhipster';
import { stringify } from 'ts-jest/dist/utils/json';
import { TickerProfile } from 'app/modules/stocks/Profile/TickerProfile';
import Chart from 'app/modules/stocks/Chart/Chart';
import useInterval from 'react-useinterval';
import { addSubscriptions } from 'app/modules/websocket/websocket-reducer';

let finnHubTimer = null;
let yahooFinanceTimer = null;

export const Profile = (props: RouteComponentProps<{ symbol: string }>) => {
  const dispatch = useAppDispatch();
  const urlParams = useParams();
  const isSocketOpen = useAppSelector(state => state.socket.isSocketOpen);
  const tickerData = useAppSelector(state => state.ticker.entities);
  // const chartData = useAppSelector(state => state.ticker.chart);
  const profile = useAppSelector(state => state.tickerProfile.entity);

  const timedUpdateFn = getMoreDetails => {
    dispatch(getStock({ symbol: urlParams['symbol'], getMoreDetails: getMoreDetails, isSocketActive: isSocketOpen }));
  };

  useEffect(() => {
    dispatch(addSymbol(urlParams['symbol']));
    // dispatch(addSubscriptions([urlParams['symbol']]))
    dispatch(getStock({ symbol: urlParams['symbol'], getMoreDetails: true, isSocketActive: isSocketOpen }));
    dispatch(getProfile(urlParams['symbol']));
    // dispatch(getChart({symbol: urlParams['symbol'], range: "Y"}));
  }, []);

  finnHubTimer = useInterval(timedUpdateFn, 12000, false); // get finnhub data
  yahooFinanceTimer = useInterval(timedUpdateFn, 60000, true); // every 5 minutes also get yahoo finance data and check every minute

  return (
    // <Chart chartData={chartData}/>
    <TickerProfile ticker={tickerData[urlParams['symbol']]} profileData={profile} />
  );
};

export default Profile;
