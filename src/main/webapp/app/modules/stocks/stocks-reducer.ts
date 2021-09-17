import { createAction, createAsyncThunk, createSlice, isFulfilled, isPending, PayloadAction } from '@reduxjs/toolkit';
import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import axios from 'axios';
import { parse } from 'ts-jest/dist/utils/json';
import { log } from 'react-jhipster';

const initialState = {
  loading: false,
  errorMessage: null,
  entities: {},
  entity: {},
  symbols: [],
  chart: [],
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'http://localhost:9000/api/';

let isSocketOpen = false;

// do not update these fields from REST calls when the finnhub websocket is active (isSocketOpen: true)
let doNotUpdateArr = ['price', 'previousClose', 'change', 'percentChange'];

export type IGetStock = { symbol: string; getMoreDetails: boolean; isSocketActive: boolean };
export type IGetStocks = { tickerSymbols: string; getMoreDetails: boolean; isSocketActive: boolean };
export type IGetSymbols = { size: number; getAll: boolean };
export type IGetChart = { symbol: string; range: string };

export const getStock = createAsyncThunk(
  'ticker/fetch_ticker',
  async ({ symbol, getMoreDetails, isSocketActive }: IGetStock) => {
    isSocketOpen = isSocketActive;
    const requestUrl = `${apiUrl}ticker?symbol=${symbol}&getMoreDetails=${getMoreDetails}`;
    return axios.get(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const getStocks = createAsyncThunk(
  'ticker/fetch_tickers',
  async ({ tickerSymbols, getMoreDetails, isSocketActive }: IGetStocks) => {
    isSocketOpen = isSocketActive;
    const requestUrl = `${apiUrl}tickers?symbols=${tickerSymbols}&getMoreDetails=${getMoreDetails}`;
    return axios.get(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const addSymbol = createAction<string>('ticker/add_symbol');

export const loadStockSymbols = createAsyncThunk('ticker/load_symbols', ({ size, getAll }: IGetSymbols) => {
  const requestUrl = `${apiUrl}symbols?size=${size}&${getAll}`;
  return axios.get(requestUrl);
});

export const removeLoadedSymbols = createAction('ticker/remove_loaded_symbols');

export const updateStocksLive = createAction<[Array<string>, object]>('ticker/update_tickers');

export const getChart = createAsyncThunk(
  'ticker/get_chart',
  async ({ symbol, range }: IGetChart) => {
    const requestUrl = `${apiUrl}chart?symbol=${symbol}&range=${range}`;
    return axios.get(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const StockSlice = createSlice({
  name: 'ticker',
  initialState,
  reducers: {
    add_symbol(state, action) {
      state.symbols = [...new Set([...state.symbols, action.payload])];
    },
    update_tickers(state, action) {
      let stockData, stockQuote;
      let symbols = action.payload[0];

      symbols.map(symbol => {
        stockData = state.entities[symbol];
        stockQuote = action.payload[1];
        if (stockQuote && stockData) {
          stockQuote = stockQuote.find(quote => quote.s == symbol);
          if (stockQuote) {
            stockData['price'] = stockQuote['p'];
            stockData['change'] = stockQuote['p'] - stockData['previousClose'];
            stockData['percentChange'] = ((stockQuote['p'] - stockData['previousClose']) / stockData['previousClose']) * 100;
          }
        }
      });
    },
    remove_loaded_symbols(state) {
      state.symbols = [];
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getStock.fulfilled, (state, action) => {
        state.loading = false;
        state.entities = { ...state.entities };
      })
      .addCase(getStocks.fulfilled, (state, action) => {
        state.loading = false;
        state.entities = { ...state.entities };
      })
      .addCase(loadStockSymbols.fulfilled, (state, action) => {
        state.loading = false;
        state.symbols = [...new Set([...state.symbols, ...action.payload.data])];
      })
      .addCase(getChart.fulfilled, (state, action) => {
        state.loading = false;
        state.chart = [...action.payload.data];
      })
      .addMatcher(isFulfilled(getStock), (state, action) => {
        // @ts-ignore
        const tickerSymbol = action.payload.data['tickerSymbol'];

        if (isSocketOpen) {
          Object.keys(action.payload.data).forEach(function (key) {
            if (doNotUpdateArr.includes(key)) {
              delete action.payload.data[key];
            }
          });
        }

        return {
          ...state,
          loading: false,
          entities: { ...state.entities, [tickerSymbol]: { ...state.entities[tickerSymbol], ...action.payload.data } },
        };
      })
      .addMatcher(isFulfilled(getStocks), (state, action) => {
        // @ts-ignore
        log('test', action.payload.data);
        const updateData = () => {
          let map = {};
          let tickerSymbols = Object.keys(action.payload.data);

          tickerSymbols.forEach(symbol => {
            if (isSocketOpen) {
              Object.keys(action.payload.data[symbol]).forEach(function (key) {
                if (doNotUpdateArr.includes(key)) {
                  delete action.payload.data[symbol][key];
                }
              });
            }

            map[symbol] = { ...state.entities[symbol], ...action.payload.data[symbol] };
          });

          return map;
        };

        return {
          ...state,
          loading: false,
          entities: { ...state.entities, ...updateData() },
        };
      })
      .addMatcher(isPending(getStock, getStocks, loadStockSymbols, getChart), state => {
        state.errorMessage = null;
        state.loading = true;
      })
      .addMatcher(isFulfilled(loadStockSymbols), (state, action) => {
        // @ts-ignore
        return {
          ...state,
          loading: false,
          symbols: [...new Set([...state.symbols, ...action.payload.data])],
        };
      })
      .addMatcher(isFulfilled(getChart), (state, action) => {
        return {
          ...state,
          loading: false,
          chart: [...action.payload.data],
        };
      });
  },
});

export default StockSlice.reducer;
