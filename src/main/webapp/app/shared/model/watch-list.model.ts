import { IUser } from 'app/shared/model/user.model';
import { ITicker } from 'app/shared/model/ticker.model';

export interface IWatchList {
  id?: number;
  user?: IUser | null;
  tickers?: ITicker[] | null;
}

export const defaultValue: Readonly<IWatchList> = {};
