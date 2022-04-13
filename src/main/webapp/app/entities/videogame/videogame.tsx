import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVideogame } from 'app/shared/model/videogame.model';
import { getEntities } from './videogame.reducer';

export const Videogame = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const videogameList = useAppSelector(state => state.videogame.entities);
  const loading = useAppSelector(state => state.videogame.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="videogame-heading" data-cy="VideogameHeading">
        <Translate contentKey="achievementApplicationApp.videogame.home.title">Videogames</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="achievementApplicationApp.videogame.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/videogame/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="achievementApplicationApp.videogame.home.createLabel">Create new Videogame</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {videogameList && videogameList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="achievementApplicationApp.videogame.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="achievementApplicationApp.videogame.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="achievementApplicationApp.videogame.achievement">Achievement</Translate>
                </th>
                <th>
                  <Translate contentKey="achievementApplicationApp.videogame.personalizedAchievement">Personalized Achievement</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {videogameList.map((videogame, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/videogame/${videogame.id}`} color="link" size="sm">
                      {videogame.id}
                    </Button>
                  </td>
                  <td>{videogame.name}</td>
                  <td>
                    {videogame.achievement ? <Link to={`/achievement/${videogame.achievement.id}`}>{videogame.achievement.id}</Link> : ''}
                  </td>
                  <td>
                    {videogame.personalizedAchievement ? (
                      <Link to={`/personalized-achievement/${videogame.personalizedAchievement.id}`}>
                        {videogame.personalizedAchievement.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/videogame/${videogame.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/videogame/${videogame.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/videogame/${videogame.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="achievementApplicationApp.videogame.home.notFound">No Videogames found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Videogame;
