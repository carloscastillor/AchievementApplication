import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IResource } from 'app/shared/model/resource.model';
import { getEntities as getResources } from 'app/entities/resource/resource.reducer';
import { IAchievement } from 'app/shared/model/achievement.model';
import { getEntities as getAchievements } from 'app/entities/achievement/achievement.reducer';
import { IWalkthrough } from 'app/shared/model/walkthrough.model';
import { getEntity, updateEntity, createEntity, reset } from './walkthrough.reducer';

export const WalkthroughUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const resources = useAppSelector(state => state.resource.entities);
  const achievements = useAppSelector(state => state.achievement.entities);
  const walkthroughEntity = useAppSelector(state => state.walkthrough.entity);
  const loading = useAppSelector(state => state.walkthrough.loading);
  const updating = useAppSelector(state => state.walkthrough.updating);
  const updateSuccess = useAppSelector(state => state.walkthrough.updateSuccess);
  const handleClose = () => {
    props.history.push('/walkthrough');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getResources({}));
    dispatch(getAchievements({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...walkthroughEntity,
      ...values,
      resource: resources.find(it => it.id.toString() === values.resource.toString()),
      user: achievements.find(it => it.id.toString() === values.user.toString()),
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
          ...walkthroughEntity,
          resource: walkthroughEntity?.resource?.id,
          user: walkthroughEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="achievementApplicationApp.walkthrough.home.createOrEditLabel" data-cy="WalkthroughCreateUpdateHeading">
            <Translate contentKey="achievementApplicationApp.walkthrough.home.createOrEditLabel">Create or edit a Walkthrough</Translate>
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
                  id="walkthrough-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('achievementApplicationApp.walkthrough.title')}
                id="walkthrough-title"
                name="title"
                data-cy="title"
                type="text"
              />
              <ValidatedField
                label={translate('achievementApplicationApp.walkthrough.description')}
                id="walkthrough-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="walkthrough-resource"
                name="resource"
                data-cy="resource"
                label={translate('achievementApplicationApp.walkthrough.resource')}
                type="select"
              >
                <option value="" key="0" />
                {resources
                  ? resources.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="walkthrough-user"
                name="user"
                data-cy="user"
                label={translate('achievementApplicationApp.walkthrough.user')}
                type="select"
              >
                <option value="" key="0" />
                {achievements
                  ? achievements.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/walkthrough" replace color="info">
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

export default WalkthroughUpdate;
