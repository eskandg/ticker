import { IUser } from 'app/shared/model/user.model';

export interface IWatchList {
  id?: number;
  tickerSymbol?: string;
  user?: IUser | null;
}

export const defaultValue: Readonly<IWatchList> = {};
