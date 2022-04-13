import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './videogame.reducer';

export const VideogameDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const videogameEntity = useAppSelector(state => state.videogame.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="videogameDetailsHeading">
          <Translate contentKey="achievementApplicationApp.videogame.detail.title">Videogame</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{videogameEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="achievementApplicationApp.videogame.name">Name</Translate>
            </span>
          </dt>
          <dd>{videogameEntity.name}</dd>
          <dt>
            <Translate contentKey="achievementApplicationApp.videogame.achievement">Achievement</Translate>
          </dt>
          <dd>{videogameEntity.achievement ? videogameEntity.achievement.id : ''}</dd>
          <dt>
            <Translate contentKey="achievementApplicationApp.videogame.personalizedAchievement">Personalized Achievement</Translate>
          </dt>
          <dd>{videogameEntity.personalizedAchievement ? videogameEntity.personalizedAchievement.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/videogame" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/videogame/${videogameEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VideogameDetail;
