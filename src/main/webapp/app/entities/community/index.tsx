import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Community from './community';
import CommunityDetail from './community-detail';
import CommunityUpdate from './community-update';
import CommunityDeleteDialog from './community-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CommunityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CommunityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CommunityDetail} />
      <ErrorBoundaryRoute path={match.url} component={Community} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CommunityDeleteDialog} />
  </>
);

export default Routes;
