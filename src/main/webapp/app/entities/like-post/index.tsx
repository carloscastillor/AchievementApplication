import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LikePost from './like-post';
import LikePostDetail from './like-post-detail';
import LikePostUpdate from './like-post-update';
import LikePostDeleteDialog from './like-post-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LikePostUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LikePostUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LikePostDetail} />
      <ErrorBoundaryRoute path={match.url} component={LikePost} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LikePostDeleteDialog} />
  </>
);

export default Routes;
