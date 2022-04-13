import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAchievement } from 'app/shared/model/achievement.model';
import { getEntity, updateEntity, createEntity, reset } from './achievement.reducer';

export const AchievementUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const achievementEntity = useAppSelector(state => state.achievement.entity);
  const loading = useAppSelector(state => state.achievement.loading);
  const updating = useAppSelector(state => state.achievement.updating);
  const updateSuccess = useAppSelector(state => state.achievement.updateSuccess);
  const handleClose = () => {
    props.history.push('/achievement');
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
      ...achievementEntity,
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
          ...achievementEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="achievementApplicationApp.achievement.home.createOrEditLabel" data-cy="AchievementCreateUpdateHeading">
            <Translate contentKey="achievementApplicationApp.achievement.home.createOrEditLabel">Create or edit a Achievement</Translate>
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
                  id="achievement-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('achievementApplicationApp.achievement.name')}
                id="achievement-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('achievementApplicationApp.achievement.description')}
                id="achievement-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('achievementApplicationApp.achievement.videogame')}
                id="achievement-videogame"
                name="videogame"
                data-cy="videogame"
                type="text"
              />
              <ValidatedField
                label={translate('achievementApplicationApp.achievement.completed')}
                id="achievement-completed"
                name="completed"
                data-cy="completed"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/achievement" replace color="info">
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

export default AchievementUpdate;
