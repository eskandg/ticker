import { ITicker } from 'app/shared/model/ticker.model';

export interface ITickerProfile {
  id?: number;
  tickerSymbol?: string;
  industry?: string | null;
  name?: string | null;
  phone?: string | null;
  website?: string | null;
  description?: string | null;
  fullTimeEmployees?: number | null;
  ticker?: ITicker | null;
}

export const defaultValue: Readonly<ITickerProfile> = {};
