import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILikeMessage } from 'app/shared/model/like-message.model';
import { getEntities } from './like-message.reducer';

export const LikeMessage = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const likeMessageList = useAppSelector(state => state.likeMessage.entities);
  const loading = useAppSelector(state => state.likeMessage.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="like-message-heading" data-cy="LikeMessageHeading">
        <Translate contentKey="achievementApplicationApp.likeMessage.home.title">Like Messages</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="achievementApplicationApp.likeMessage.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/like-message/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="achievementApplicationApp.likeMessage.home.createLabel">Create new Like Message</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {likeMessageList && likeMessageList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="achievementApplicationApp.likeMessage.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="achievementApplicationApp.likeMessage.like">Like</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {likeMessageList.map((likeMessage, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/like-message/${likeMessage.id}`} color="link" size="sm">
                      {likeMessage.id}
                    </Button>
                  </td>
                  <td>{likeMessage.like ? 'true' : 'false'}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/like-message/${likeMessage.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/like-message/${likeMessage.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/like-message/${likeMessage.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="achievementApplicationApp.likeMessage.home.notFound">No Like Messages found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default LikeMessage;
