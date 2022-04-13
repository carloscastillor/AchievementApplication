import React, { useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './like-message.reducer';

export const LikeMessageDeleteDialog = (props: RouteComponentProps<{ id: string }>) => {
  const [loadModal, setLoadModal] = useState(false);
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
    setLoadModal(true);
  }, []);

  const likeMessageEntity = useAppSelector(state => state.likeMessage.entity);
  const updateSuccess = useAppSelector(state => state.likeMessage.updateSuccess);

  const handleClose = () => {
    props.history.push('/like-message');
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(likeMessageEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="likeMessageDeleteDialogHeading">
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="achievementApplicationApp.likeMessage.delete.question">
        <Translate contentKey="achievementApplicationApp.likeMessage.delete.question" interpolate={{ id: likeMessageEntity.id }}>
          Are you sure you want to delete this LikeMessage?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-likeMessage" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default LikeMessageDeleteDialog;
