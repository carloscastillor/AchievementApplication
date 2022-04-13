import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPost } from 'app/shared/model/post.model';
import { getEntities } from './post.reducer';

export const Post = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const postList = useAppSelector(state => state.post.entities);
  const loading = useAppSelector(state => state.post.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="post-heading" data-cy="PostHeading">
        <Translate contentKey="achievementApplicationApp.post.home.title">Posts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="achievementApplicationApp.post.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/post/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="achievementApplicationApp.post.home.createLabel">Create new Post</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {postList && postList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="achievementApplicationApp.post.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="achievementApplicationApp.post.title">Title</Translate>
                </th>
                <th>
                  <Translate contentKey="achievementApplicationApp.post.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="achievementApplicationApp.post.message">Message</Translate>
                </th>
                <th>
                  <Translate contentKey="achievementApplicationApp.post.likePost">Like Post</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {postList.map((post, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/post/${post.id}`} color="link" size="sm">
                      {post.id}
                    </Button>
                  </td>
                  <td>{post.title}</td>
                  <td>{post.description}</td>
                  <td>{post.message ? <Link to={`/message/${post.message.id}`}>{post.message.id}</Link> : ''}</td>
                  <td>{post.likePost ? <Link to={`/like-post/${post.likePost.id}`}>{post.likePost.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/post/${post.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/post/${post.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/post/${post.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="achievementApplicationApp.post.home.notFound">No Posts found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Post;
