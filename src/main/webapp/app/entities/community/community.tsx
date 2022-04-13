import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICommunity } from 'app/shared/model/community.model';
import { getEntities } from './community.reducer';

export const Community = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const communityList = useAppSelector(state => state.community.entities);
  const loading = useAppSelector(state => state.community.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="community-heading" data-cy="CommunityHeading">
        <Translate contentKey="achievementApplicationApp.community.home.title">Communities</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="achievementApplicationApp.community.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/community/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="achievementApplicationApp.community.home.createLabel">Create new Community</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {communityList && communityList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="achievementApplicationApp.community.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="achievementApplicationApp.community.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="achievementApplicationApp.community.post">Post</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {communityList.map((community, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/community/${community.id}`} color="link" size="sm">
                      {community.id}
                    </Button>
                  </td>
                  <td>{community.name}</td>
                  <td>{community.post ? <Link to={`/post/${community.post.id}`}>{community.post.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/community/${community.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/community/${community.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/community/${community.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="achievementApplicationApp.community.home.notFound">No Communities found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Community;
