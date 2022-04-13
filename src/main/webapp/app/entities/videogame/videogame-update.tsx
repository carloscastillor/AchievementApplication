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
import { getEntities as getPersonalizedAchievements } from 'app/entities/personalized-achievement/personalized-achievement.reducer';
import { IVideogame } from 'app/shared/model/videogame.model';
import { getEntity, updateEntity, createEntity, reset } from './videogame.reducer';

export const VideogameUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const achievements = useAppSelector(state => state.achievement.entities);
  const personalizedAchievements = useAppSelector(state => state.personalizedAchievement.entities);
  const videogameEntity = useAppSelector(state => state.videogame.entity);
  const loading = useAppSelector(state => state.videogame.loading);
  const updating = useAppSelector(state => state.videogame.updating);
  const updateSuccess = useAppSelector(state => state.videogame.updateSuccess);
  const handleClose = () => {
    props.history.push('/videogame');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getAchievements({}));
    dispatch(getPersonalizedAchievements({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...videogameEntity,
      ...values,
      achievement: achievements.find(it => it.id.toString() === values.achievement.toString()),
      personalizedAchievement: personalizedAchievements.find(it => it.id.toString() === values.personalizedAchievement.toString()),
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
          ...videogameEntity,
          achievement: videogameEntity?.achievement?.id,
          personalizedAchievement: videogameEntity?.personalizedAchievement?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="achievementApplicationApp.videogame.home.createOrEditLabel" data-cy="VideogameCreateUpdateHeading">
            <Translate contentKey="achievementApplicationApp.videogame.home.createOrEditLabel">Create or edit a Videogame</Translate>
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
                  id="videogame-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('achievementApplicationApp.videogame.name')}
                id="videogame-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                id="videogame-achievement"
                name="achievement"
                data-cy="achievement"
                label={translate('achievementApplicationApp.videogame.achievement')}
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
              <ValidatedField
                id="videogame-personalizedAchievement"
                name="personalizedAchievement"
                data-cy="personalizedAchievement"
                label={translate('achievementApplicationApp.videogame.personalizedAchievement')}
                type="select"
              >
                <option value="" key="0" />
                {personalizedAchievements
                  ? personalizedAchievements.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/videogame" replace color="info">
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

export default VideogameUpdate;
