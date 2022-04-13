import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Walkthrough from './walkthrough';
import WalkthroughDetail from './walkthrough-detail';
import WalkthroughUpdate from './walkthrough-update';
import WalkthroughDeleteDialog from './walkthrough-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WalkthroughUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WalkthroughUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WalkthroughDetail} />
      <ErrorBoundaryRoute path={match.url} component={Walkthrough} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WalkthroughDeleteDialog} />
  </>
);

export default Routes;
