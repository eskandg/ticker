import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ITicker } from 'app/shared/model/ticker.model';
import { getEntities as getTickers } from 'app/entities/ticker/ticker.reducer';
import { getEntity, updateEntity, createEntity, reset } from './ticker-profile.reducer';
import { ITickerProfile } from 'app/shared/model/ticker-profile.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TickerProfileUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const tickers = useAppSelector(state => state.ticker.entities);
  const tickerProfileEntity = useAppSelector(state => state.tickerProfile.entity);
  const loading = useAppSelector(state => state.tickerProfile.loading);
  const updating = useAppSelector(state => state.tickerProfile.updating);
  const updateSuccess = useAppSelector(state => state.tickerProfile.updateSuccess);

  const handleClose = () => {
    props.history.push('/ticker-profile');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTickers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...tickerProfileEntity,
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
          ...tickerProfileEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tickerApp.tickerProfile.home.createOrEditLabel" data-cy="TickerProfileCreateUpdateHeading">
            Create or edit a TickerProfile
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
                <ValidatedField name="id" required readOnly id="ticker-profile-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Ticker Symbol"
                id="ticker-profile-tickerSymbol"
                name="tickerSymbol"
                data-cy="tickerSymbol"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Logo Url" id="ticker-profile-logoUrl" name="logoUrl" data-cy="logoUrl" type="text" />
              <ValidatedField label="Industry" id="ticker-profile-industry" name="industry" data-cy="industry" type="text" />
              <ValidatedField label="Name" id="ticker-profile-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Phone" id="ticker-profile-phone" name="phone" data-cy="phone" type="text" />
              <ValidatedField label="Website" id="ticker-profile-website" name="website" data-cy="website" type="text" />
              <ValidatedField
                label="Description"
                id="ticker-profile-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label="Full Time Employees"
                id="ticker-profile-fullTimeEmployees"
                name="fullTimeEmployees"
                data-cy="fullTimeEmployees"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ticker-profile" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TickerProfileUpdate;
