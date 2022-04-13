import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LikeMessage from './like-message';
import LikeMessageDetail from './like-message-detail';
import LikeMessageUpdate from './like-message-update';
import LikeMessageDeleteDialog from './like-message-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LikeMessageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LikeMessageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LikeMessageDetail} />
      <ErrorBoundaryRoute path={match.url} component={LikeMessage} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LikeMessageDeleteDialog} />
  </>
);

export default Routes;
