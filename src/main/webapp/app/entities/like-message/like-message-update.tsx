import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILikeMessage } from 'app/shared/model/like-message.model';
import { getEntity, updateEntity, createEntity, reset } from './like-message.reducer';

export const LikeMessageUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const likeMessageEntity = useAppSelector(state => state.likeMessage.entity);
  const loading = useAppSelector(state => state.likeMessage.loading);
  const updating = useAppSelector(state => state.likeMessage.updating);
  const updateSuccess = useAppSelector(state => state.likeMessage.updateSuccess);
  const handleClose = () => {
    props.history.push('/like-message');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...likeMessageEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...likeMessageEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="achievementApplicationApp.likeMessage.home.createOrEditLabel" data-cy="LikeMessageCreateUpdateHeading">
            <Translate contentKey="achievementApplicationApp.likeMessage.home.createOrEditLabel">Create or edit a LikeMessage</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="like-message-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('achievementApplicationApp.likeMessage.like')}
                id="like-message-like"
                name="like"
                data-cy="like"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/like-message" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default LikeMessageUpdate;
