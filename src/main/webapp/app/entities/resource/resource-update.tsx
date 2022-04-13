import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IResource } from 'app/shared/model/resource.model';
import { getEntity, updateEntity, createEntity, reset } from './resource.reducer';

export const ResourceUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const resourceEntity = useAppSelector(state => state.resource.entity);
  const loading = useAppSelector(state => state.resource.loading);
  const updating = useAppSelector(state => state.resource.updating);
  const updateSuccess = useAppSelector(state => state.resource.updateSuccess);
  const handleClose = () => {
    props.history.push('/resource');
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
      ...resourceEntity,
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
          ...resourceEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="achievementApplicationApp.resource.home.createOrEditLabel" data-cy="ResourceCreateUpdateHeading">
            <Translate contentKey="achievementApplicationApp.resource.home.createOrEditLabel">Create or edit a Resource</Translate>
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
                  id="resource-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('achievementApplicationApp.resource.title')}
                id="resource-title"
                name="title"
                data-cy="title"
                type="text"
              />
              <ValidatedField
                label={translate('achievementApplicationApp.resource.description')}
                id="resource-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('achievementApplicationApp.resource.urlSource')}
                id="resource-urlSource"
                name="urlSource"
                data-cy="urlSource"
                type="text"
              />
              <ValidatedField
                label={translate('achievementApplicationApp.resource.resourceType')}
                id="resource-resourceType"
                name="resourceType"
                data-cy="resourceType"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/resource" replace color="info">
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

export default ResourceUpdate;
