import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './achievement.reducer';

export const AchievementDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const achievementEntity = useAppSelector(state => state.achievement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="achievementDetailsHeading">
          <Translate contentKey="achievementApplicationApp.achievement.detail.title">Achievement</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{achievementEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="achievementApplicationApp.achievement.name">Name</Translate>
            </span>
          </dt>
          <dd>{achievementEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="achievementApplicationApp.achievement.description">Description</Translate>
            </span>
          </dt>
          <dd>{achievementEntity.description}</dd>
          <dt>
            <span id="videogame">
              <Translate contentKey="achievementApplicationApp.achievement.videogame">Videogame</Translate>
            </span>
          </dt>
          <dd>{achievementEntity.videogame}</dd>
          <dt>
            <span id="completed">
              <Translate contentKey="achievementApplicationApp.achievement.completed">Completed</Translate>
            </span>
          </dt>
          <dd>{achievementEntity.completed ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/achievement" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/achievement/${achievementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AchievementDetail;
