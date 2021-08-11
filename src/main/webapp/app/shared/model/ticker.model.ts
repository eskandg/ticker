import dayjs from 'dayjs';
import { ITickerProfile } from 'app/shared/model/ticker-profile.model';
import { IWatchList } from 'app/shared/model/watch-list.model';

export interface ITicker {
  id?: number;
  symbol?: string;
  updatedAt?: string | null;
  change?: number | null;
  marketPrice?: number | null;
  marketCap?: string | null;
  volume?: number | null;
  avgVolume?: number | null;
  low?: number | null;
  high?: number | null;
  open?: number | null;
  close?: number | null;
  previousClose?: number | null;
  bid?: number | null;
  ask?: number | null;
  bidVol?: number | null;
  askVol?: number | null;
  dayLow?: number | null;
  dayHigh?: number | null;
  fiftyTwoWeekLow?: number | null;
  fiftyTwoWeekHigh?: number | null;
  beta?: number | null;
  peRatio?: number | null;
  eps?: number | null;
  tickerSymbol?: ITickerProfile | null;
  watchedIns?: IWatchList[] | null;
}

export const defaultValue: Readonly<ITicker> = {};
