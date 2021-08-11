import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TickerProfile from './ticker-profile';
import TickerProfileDetail from './ticker-profile-detail';
import TickerProfileUpdate from './ticker-profile-update';
import TickerProfileDeleteDialog from './ticker-profile-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TickerProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TickerProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TickerProfileDetail} />
      <ErrorBoundaryRoute path={match.url} component={TickerProfile} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TickerProfileDeleteDialog} />
  </>
);

export default Routes;
