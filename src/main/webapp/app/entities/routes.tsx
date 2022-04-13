import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Resource from './resource';
import Walkthrough from './walkthrough';
import Message from './message';
import Videogame from './videogame';
import Achievement from './achievement';
import PersonalizedAchievement from './personalized-achievement';
import Community from './community';
import Post from './post';
import LikePost from './like-post';
import LikeMessage from './like-message';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}resource`} component={Resource} />
        <ErrorBoundaryRoute path={`${match.url}walkthrough`} component={Walkthrough} />
        <ErrorBoundaryRoute path={`${match.url}message`} component={Message} />
        <ErrorBoundaryRoute path={`${match.url}videogame`} component={Videogame} />
        <ErrorBoundaryRoute path={`${match.url}achievement`} component={Achievement} />
        <ErrorBoundaryRoute path={`${match.url}personalized-achievement`} component={PersonalizedAchievement} />
        <ErrorBoundaryRoute path={`${match.url}community`} component={Community} />
        <ErrorBoundaryRoute path={`${match.url}post`} component={Post} />
        <ErrorBoundaryRoute path={`${match.url}like-post`} component={LikePost} />
        <ErrorBoundaryRoute path={`${match.url}like-message`} component={LikeMessage} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
