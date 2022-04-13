import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PersonalizedAchievement from './personalized-achievement';
import PersonalizedAchievementDetail from './personalized-achievement-detail';
import PersonalizedAchievementUpdate from './personalized-achievement-update';
import PersonalizedAchievementDeleteDialog from './personalized-achievement-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PersonalizedAchievementUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PersonalizedAchievementUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PersonalizedAchievementDetail} />
      <ErrorBoundaryRoute path={match.url} component={PersonalizedAchievement} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PersonalizedAchievementDeleteDialog} />
  </>
);

export default Routes;
