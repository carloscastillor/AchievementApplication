import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './community.reducer';

export const CommunityDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const communityEntity = useAppSelector(state => state.community.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="communityDetailsHeading">
          <Translate contentKey="achievementApplicationApp.community.detail.title">Community</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{communityEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="achievementApplicationApp.community.name">Name</Translate>
            </span>
          </dt>
          <dd>{communityEntity.name}</dd>
          <dt>
            <Translate contentKey="achievementApplicationApp.community.post">Post</Translate>
          </dt>
          <dd>{communityEntity.post ? communityEntity.post.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/community" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/community/${communityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommunityDetail;
