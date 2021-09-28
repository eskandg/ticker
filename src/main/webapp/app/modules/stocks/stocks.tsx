import React, { useEffect, useState } from 'react';
import { useAppSelector, useAppDispatch } from 'app/config/store';
import { getEntities as getProfiles, getEntity as getTickerProfile } from '../../entities/ticker-profile/ticker-profile.reducer';

import { Button, Card, CardBody, CardImg, CardText, CardTitle, Col, Row } from 'reactstrap';
import { Ticker } from './Ticker/Ticker';
import { getStocks, loadStockSymbols } from 'app/modules/stocks/stocks-reducer';
import { getSortState, log } from 'react-jhipster';
import ReconnectingWebSocket from 'reconnecting-websocket';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { RouteComponentProps, useLocation } from 'react-router-dom';
import { getCurrentRoute } from 'app/shared/reducers/routes';
import useInterval from 'react-useinterval';

let socket = null;

let mount = false;
let watchListLoading = false;
export const Stocks = (props: RouteComponentProps<any>) => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const profiles = useAppSelector(state => state.tickerProfile.entities);
  const tickers = useAppSelector(state => state.ticker.entities);
  const userFollows = useAppSelector(state => state.watchList.userFollows);
  const symbols = useAppSelector(state => state.ticker.symbols);

  const isSocketOpen = useAppSelector(state => state.socket.isSocketOpen);

  // const [pagination, setPagination] = useState(
  //   overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  // );
  //
  // const endURL = `?page=${pagination.activePage}&sort=${pagination.sort},${pagination.order}`;
  // if (props.location.search !== endURL) {
  //   props.history.push(`${props.location.pathname}${endURL}`);
  // }
  let finnHubTimer, yahooFinanceTimer;
  const timedUpdateFn = getMoreDetails => {
    if (symbols.length > 0) {
      dispatch(getStocks({ tickerSymbols: symbols.join(','), getMoreDetails: getMoreDetails, isSocketActive: isSocketOpen }));
    }
  };
  useEffect(() => {
    if (symbols.length > 0) {
      const userFollowsSet = new Set(userFollows);
      for (let i = 0; i < symbols.length; i++) {
        if (userFollowsSet.has(symbols[i])) {
          watchListLoading = true;
          break;
        }
      }

      if (!watchListLoading) {
        dispatch(getStocks({ tickerSymbols: symbols.join(','), getMoreDetails: true, isSocketActive: isSocketOpen }));
      }
    }
  }, [symbols]);

  useEffect(() => {
    if (mount && symbols.length > 0) {
      dispatch(getStocks({ tickerSymbols: symbols.join(','), getMoreDetails: true, isSocketActive: false }));
      mount = false;
    }
  }, [mount, symbols]);

  useEffect(() => {
    dispatch(loadStockSymbols({ size: 10, getAll: false }));
    mount = true;
  }, []);

  finnHubTimer = useInterval(timedUpdateFn, 12000, false); // get finnhub data
  yahooFinanceTimer = useInterval(timedUpdateFn, 60000, true); // every 5 minutes also get yahoo finance data and check every minute

  return (
    <>
      <Row>{symbols.map(tickerSymbol => tickers[tickerSymbol] && <Ticker key={tickerSymbol} ticker={tickers[tickerSymbol]} />)}</Row>
    </>
  );
};

export default Stocks;
