import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAchievement } from 'app/shared/model/achievement.model';
import { getEntities as getAchievements } from 'app/entities/achievement/achievement.reducer';
import { IPersonalizedAchievement } from 'app/shared/model/personalized-achievement.model';
import { getEntity, updateEntity, createEntity, reset } from './personalized-achievement.reducer';

export const PersonalizedAchievementUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const achievements = useAppSelector(state => state.achievement.entities);
  const personalizedAchievementEntity = useAppSelector(state => state.personalizedAchievement.entity);
  const loading = useAppSelector(state => state.personalizedAchievement.loading);
  const updating = useAppSelector(state => state.personalizedAchievement.updating);
  const updateSuccess = useAppSelector(state => state.personalizedAchievement.updateSuccess);
  const handleClose = () => {
    props.history.push('/personalized-achievement');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getAchievements({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...personalizedAchievementEntity,
      ...values,
      achievement: achievements.find(it => it.id.toString() === values.achievement.toString()),
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
          ...personalizedAchievementEntity,
          achievement: personalizedAchievementEntity?.achievement?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2
            id="achievementApplicationApp.personalizedAchievement.home.createOrEditLabel"
            data-cy="PersonalizedAchievementCreateUpdateHeading"
          >
            <Translate contentKey="achievementApplicationApp.personalizedAchievement.home.createOrEditLabel">
              Create or edit a PersonalizedAchievement
            </Translate>
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
                  id="personalized-achievement-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                id="personalized-achievement-achievement"
                name="achievement"
                data-cy="achievement"
                label={translate('achievementApplicationApp.personalizedAchievement.achievement')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/personalized-achievement" replace color="info">
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

export default PersonalizedAchievementUpdate;
