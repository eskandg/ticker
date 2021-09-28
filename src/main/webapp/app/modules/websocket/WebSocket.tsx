import React, { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import ReconnectingWebSocket from 'reconnecting-websocket';
import { addSubscriptions, setIsSocketOpen } from 'app/modules/websocket/websocket-reducer';
import { removeLoadedSymbols, updateStocksLive } from 'app/modules/stocks/stocks-reducer';
import { log } from 'react-jhipster';

// Websocket component to be used at the top of the app

let socket = null;
export const WebSocket = () => {
  const dispatch = useAppDispatch();
  const location = useAppSelector(state => state.routes.location);
  const subscriptions = useAppSelector(state => state.socket.subscriptions);
  const symbols = useAppSelector(state => state.ticker.symbols);
  const userFollows = useAppSelector(state => state.watchList.userFollows);

  const [liveQuoteData, setLiveQuoteData] = useState();

  function subscriptionHandler() {
    let usedSymbols = [...new Set([...symbols, ...userFollows])];

    usedSymbols.map(symbol => {
      if (!new Set(subscriptions).has(symbol)) {
        dispatch(addSubscriptions([symbol]));
        socket.send(JSON.stringify({ type: 'subscribe', symbol: `${symbol}` }));
      }
    });
  }

  useEffect(() => {
    return () => {
      if (socket !== null) {
        dispatch(setIsSocketOpen(false));
        socket.close();
      }
    };
  }, []);

  useEffect(() => {
    if (socket === null) socket = new ReconnectingWebSocket(`ws://localhost:8000`);

    socket.onopen = () => {
      dispatch(setIsSocketOpen(true));

      socket.addEventListener('message', function (event) {
        const msgObj = JSON.parse(event.data);
        if (msgObj.type !== 'ping') {
          setLiveQuoteData(msgObj.data);
        }
      });

      // subscriptionHandler();
    };

    socket.onclose = () => {
      dispatch(setIsSocketOpen(null));
    };
  }, [symbols, userFollows]);

  useEffect(() => {
    // dispatch(addSubscriptions(symbols));
    subscriptionHandler();
  }, [symbols]);

  useEffect(() => {
    if (symbols !== undefined && symbols.length > 0) {
      dispatch(updateStocksLive([[...new Set([...symbols, ...userFollows])], liveQuoteData]));
    }
  }, [liveQuoteData]);

  useEffect(() => {
    if (symbols && symbols.length > 0) {
      dispatch(removeLoadedSymbols());
    }
  }, [location]);

  return null;
};

export default WebSocket;
