import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IResource } from 'app/shared/model/resource.model';
import { getEntities } from './resource.reducer';

export const Resource = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const resourceList = useAppSelector(state => state.resource.entities);
  const loading = useAppSelector(state => state.resource.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="resource-heading" data-cy="ResourceHeading">
        <Translate contentKey="achievementApplicationApp.resource.home.title">Resources</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="achievementApplicationApp.resource.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/resource/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="achievementApplicationApp.resource.home.createLabel">Create new Resource</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {resourceList && resourceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="achievementApplicationApp.resource.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="achievementApplicationApp.resource.title">Title</Translate>
                </th>
                <th>
                  <Translate contentKey="achievementApplicationApp.resource.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="achievementApplicationApp.resource.urlSource">Url Source</Translate>
                </th>
                <th>
                  <Translate contentKey="achievementApplicationApp.resource.resourceType">Resource Type</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {resourceList.map((resource, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/resource/${resource.id}`} color="link" size="sm">
                      {resource.id}
                    </Button>
                  </td>
                  <td>{resource.title}</td>
                  <td>{resource.description}</td>
                  <td>{resource.urlSource}</td>
                  <td>{resource.resourceType}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/resource/${resource.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/resource/${resource.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/resource/${resource.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="achievementApplicationApp.resource.home.notFound">No Resources found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Resource;
