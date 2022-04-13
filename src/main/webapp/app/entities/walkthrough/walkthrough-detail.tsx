import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './walkthrough.reducer';

export const WalkthroughDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const walkthroughEntity = useAppSelector(state => state.walkthrough.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="walkthroughDetailsHeading">
          <Translate contentKey="achievementApplicationApp.walkthrough.detail.title">Walkthrough</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{walkthroughEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="achievementApplicationApp.walkthrough.title">Title</Translate>
            </span>
          </dt>
          <dd>{walkthroughEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="achievementApplicationApp.walkthrough.description">Description</Translate>
            </span>
          </dt>
          <dd>{walkthroughEntity.description}</dd>
          <dt>
            <Translate contentKey="achievementApplicationApp.walkthrough.resource">Resource</Translate>
          </dt>
          <dd>{walkthroughEntity.resource ? walkthroughEntity.resource.id : ''}</dd>
          <dt>
            <Translate contentKey="achievementApplicationApp.walkthrough.user">User</Translate>
          </dt>
          <dd>{walkthroughEntity.user ? walkthroughEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/walkthrough" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/walkthrough/${walkthroughEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WalkthroughDetail;
