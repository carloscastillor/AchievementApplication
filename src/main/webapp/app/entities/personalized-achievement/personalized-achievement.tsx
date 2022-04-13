import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPersonalizedAchievement } from 'app/shared/model/personalized-achievement.model';
import { getEntities } from './personalized-achievement.reducer';

export const PersonalizedAchievement = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const personalizedAchievementList = useAppSelector(state => state.personalizedAchievement.entities);
  const loading = useAppSelector(state => state.personalizedAchievement.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="personalized-achievement-heading" data-cy="PersonalizedAchievementHeading">
        <Translate contentKey="achievementApplicationApp.personalizedAchievement.home.title">Personalized Achievements</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="achievementApplicationApp.personalizedAchievement.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/personalized-achievement/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="achievementApplicationApp.personalizedAchievement.home.createLabel">
              Create new Personalized Achievement
            </Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {personalizedAchievementList && personalizedAchievementList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="achievementApplicationApp.personalizedAchievement.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="achievementApplicationApp.personalizedAchievement.achievement">Achievement</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {personalizedAchievementList.map((personalizedAchievement, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/personalized-achievement/${personalizedAchievement.id}`} color="link" size="sm">
                      {personalizedAchievement.id}
                    </Button>
                  </td>
                  <td>
                    {personalizedAchievement.achievement ? (
                      <Link to={`/achievement/${personalizedAchievement.achievement.id}`}>{personalizedAchievement.achievement.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/personalized-achievement/${personalizedAchievement.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/personalized-achievement/${personalizedAchievement.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/personalized-achievement/${personalizedAchievement.id}/delete`}
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
              <Translate contentKey="achievementApplicationApp.personalizedAchievement.home.notFound">
                No Personalized Achievements found
              </Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PersonalizedAchievement;
