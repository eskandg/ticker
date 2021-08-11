import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WatchList from './watch-list';
import WatchListDetail from './watch-list-detail';
import WatchListUpdate from './watch-list-update';
import WatchListDeleteDialog from './watch-list-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WatchListUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WatchListUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WatchListDetail} />
      <ErrorBoundaryRoute path={match.url} component={WatchList} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WatchListDeleteDialog} />
  </>
);

export default Routes;
