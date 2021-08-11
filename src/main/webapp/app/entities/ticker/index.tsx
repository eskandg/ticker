import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Ticker from './ticker';
import TickerDetail from './ticker-detail';
import TickerUpdate from './ticker-update';
import TickerDeleteDialog from './ticker-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TickerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TickerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TickerDetail} />
      <ErrorBoundaryRoute path={match.url} component={Ticker} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TickerDeleteDialog} />
  </>
);

export default Routes;
