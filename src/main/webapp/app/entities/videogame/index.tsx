import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Videogame from './videogame';
import VideogameDetail from './videogame-detail';
import VideogameUpdate from './videogame-update';
import VideogameDeleteDialog from './videogame-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VideogameUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VideogameUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VideogameDetail} />
      <ErrorBoundaryRoute path={match.url} component={Videogame} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VideogameDeleteDialog} />
  </>
);

export default Routes;
