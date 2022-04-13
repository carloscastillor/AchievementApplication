import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './personalized-achievement.reducer';

export const PersonalizedAchievementDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const personalizedAchievementEntity = useAppSelector(state => state.personalizedAchievement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="personalizedAchievementDetailsHeading">
          <Translate contentKey="achievementApplicationApp.personalizedAchievement.detail.title">PersonalizedAchievement</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{personalizedAchievementEntity.id}</dd>
          <dt>
            <Translate contentKey="achievementApplicationApp.personalizedAchievement.achievement">Achievement</Translate>
          </dt>
          <dd>{personalizedAchievementEntity.achievement ? personalizedAchievementEntity.achievement.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/personalized-achievement" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/personalized-achievement/${personalizedAchievementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PersonalizedAchievementDetail;
