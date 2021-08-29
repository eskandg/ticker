export interface ITickerProfile {
  id?: number;
  tickerSymbol?: string;
  logoUrl?: string | null;
  industry?: string | null;
  name?: string | null;
  phone?: string | null;
  website?: string | null;
  description?: string | null;
  fullTimeEmployees?: number | null;
}

export const defaultValue: Readonly<ITickerProfile> = {};
