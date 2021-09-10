import React, { useEffect, useState } from 'react';
import { useAppSelector, useAppDispatch } from 'app/config/store';
import { getEntities as getProfiles, getEntity as getTickerProfile } from '../../entities/ticker-profile/ticker-profile.reducer';

import { Card, CardBody, CardImg, CardText, CardTitle, Col, Row } from 'reactstrap';
import { Ticker } from './Ticker/Ticker';
import { getStock, getStocks, loadStockSymbols, updateStocksLive } from 'app/modules/stocks/stocks-reducer';
import { getSortState, log } from 'react-jhipster';
import ReconnectingWebSocket from 'reconnecting-websocket';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { RouteComponentProps, useLocation } from 'react-router-dom';
import { getCurrentRoute } from 'app/shared/reducers/routes';

let socket = null;
let isSocketOpen = false;

export const Stocks = (props: RouteComponentProps<any>) => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const profiles = useAppSelector(state => state.tickerProfile.entities);
  const tickers = useAppSelector(state => state.ticker.entities);
  const symbols = useAppSelector(state => state.ticker.symbols);

  // const [pagination, setPagination] = useState(
  //   overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  // );
  //
  // const endURL = `?page=${pagination.activePage}&sort=${pagination.sort},${pagination.order}`;
  // if (props.location.search !== endURL) {
  //   props.history.push(`${props.location.pathname}${endURL}`);
  // }

  const [liveQuoteData, setLiveQuoteData] = useState();

  let finnHubTimer, yahooFinanceTimer;

  const timedUpdateFn = getMoreDetails => {
    if (symbols.length > 0) {
      dispatch(getStocks({ tickerSymbols: symbols.join(','), getMoreDetails: getMoreDetails, isSocketActive: isSocketOpen }));
    }
  };

  useEffect(() => {
    if (symbols.length > 0) {
      dispatch(getStocks({ tickerSymbols: symbols.join(','), getMoreDetails: true, isSocketActive: isSocketOpen }));
    }

    if (socket !== null) {
      clearInterval(finnHubTimer);
      clearInterval(yahooFinanceTimer);
    } else if (symbols.length > 0) {
      socket = new ReconnectingWebSocket(`ws://localhost:8000`);

      socket.onopen = () => {
        isSocketOpen = true;

        symbols[0].map(symbol => {
          socket.send(JSON.stringify({ type: 'subscribe', symbol: `${symbol}` }));
        });
        socket.addEventListener('message', function (event) {
          const msgObj = JSON.parse(event.data);
          if (msgObj.type !== 'ping') {
            setLiveQuoteData(msgObj.data);
          }
        });
      };

      socket.onclose = () => {
        isSocketOpen = false;
      };
    }

    finnHubTimer = setInterval(timedUpdateFn, 12000, false); // get finnhub data
    yahooFinanceTimer = setInterval(timedUpdateFn, 60000, true); // every 5 minutes also get yahoo finance data
  }, [symbols]);

  useEffect(() => {
    if (symbols[0] !== undefined) dispatch(updateStocksLive([symbols[0], liveQuoteData]));
  }, [liveQuoteData]);

  useEffect(() => {
    dispatch(loadStockSymbols({ size: 10, getAll: false }));
    return () => {
      if (socket !== null) {
        socket.close();
      }
      clearInterval(finnHubTimer);
      clearInterval(yahooFinanceTimer);
    };
  }, []);

  return (
    <Row>
      {symbols.map(tickerSymbols =>
        tickerSymbols.map(
          tickerSymbol => tickers[tickerSymbol] && <Ticker key={tickerSymbol} ticker={tickers[tickerSymbol]} profileLogo={''} />
        )
      )}
    </Row>
  );
};

export default Stocks;
