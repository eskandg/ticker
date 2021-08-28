import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ITickerProfile } from 'app/shared/model/ticker-profile.model';
import { getProfiles as getTickerProfiles } from 'app/entities/ticker-profile/ticker-profile.reducer';
import { IWatchList } from 'app/shared/model/watch-list.model';
import { getEntities as getWatchLists } from 'app/entities/watch-list/watch-list.reducer';
import { getEntity, updateEntity, createEntity, reset } from './ticker.reducer';
import { ITicker } from 'app/shared/model/ticker.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TickerUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const tickerProfiles = useAppSelector(state => state.tickerProfile.entities);
  const watchLists = useAppSelector(state => state.watchList.entities);
  const tickerEntity = useAppSelector(state => state.ticker.entity);
  const loading = useAppSelector(state => state.ticker.loading);
  const updating = useAppSelector(state => state.ticker.updating);
  const updateSuccess = useAppSelector(state => state.ticker.updateSuccess);

  const handleClose = () => {
    props.history.push('/ticker');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTickerProfiles({}));
    dispatch(getWatchLists({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...tickerEntity,
      ...values,
      tickerSymbol: tickerProfiles.find(it => it.id.toString() === values.tickerSymbolId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...tickerEntity,
          updatedAt: convertDateTimeFromServer(tickerEntity.updatedAt),
          tickerSymbolId: tickerEntity?.tickerSymbol?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tickerApp.ticker.home.createOrEditLabel" data-cy="TickerCreateUpdateHeading">
            Create or edit a Ticker
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="ticker-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Symbol"
                id="ticker-symbol"
                name="symbol"
                data-cy="symbol"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Updated At"
                id="ticker-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Price Change" id="ticker-priceChange" name="priceChange" data-cy="priceChange" type="text" />
              <ValidatedField
                label="Price Percent Change"
                id="ticker-pricePercentChange"
                name="pricePercentChange"
                data-cy="pricePercentChange"
                type="text"
              />
              <ValidatedField label="Market Price" id="ticker-marketPrice" name="marketPrice" data-cy="marketPrice" type="text" />
              <ValidatedField label="Market Cap" id="ticker-marketCap" name="marketCap" data-cy="marketCap" type="text" />
              <ValidatedField label="Volume" id="ticker-volume" name="volume" data-cy="volume" type="text" />
              <ValidatedField label="Avg Volume" id="ticker-avgVolume" name="avgVolume" data-cy="avgVolume" type="text" />
              <ValidatedField label="Low" id="ticker-low" name="low" data-cy="low" type="text" />
              <ValidatedField label="High" id="ticker-high" name="high" data-cy="high" type="text" />
              <ValidatedField label="Open" id="ticker-open" name="open" data-cy="open" type="text" />
              <ValidatedField label="Close" id="ticker-close" name="close" data-cy="close" type="text" />
              <ValidatedField label="Previous Close" id="ticker-previousClose" name="previousClose" data-cy="previousClose" type="text" />
              <ValidatedField label="Bid" id="ticker-bid" name="bid" data-cy="bid" type="text" />
              <ValidatedField label="Ask" id="ticker-ask" name="ask" data-cy="ask" type="text" />
              <ValidatedField label="Bid Vol" id="ticker-bidVol" name="bidVol" data-cy="bidVol" type="text" />
              <ValidatedField label="Ask Vol" id="ticker-askVol" name="askVol" data-cy="askVol" type="text" />
              <ValidatedField
                label="Fifty Two Week Low"
                id="ticker-fiftyTwoWeekLow"
                name="fiftyTwoWeekLow"
                data-cy="fiftyTwoWeekLow"
                type="text"
              />
              <ValidatedField
                label="Fifty Two Week High"
                id="ticker-fiftyTwoWeekHigh"
                name="fiftyTwoWeekHigh"
                data-cy="fiftyTwoWeekHigh"
                type="text"
              />
              <ValidatedField label="Beta" id="ticker-beta" name="beta" data-cy="beta" type="text" />
              <ValidatedField label="Pe Ratio" id="ticker-peRatio" name="peRatio" data-cy="peRatio" type="text" />
              <ValidatedField label="Eps" id="ticker-eps" name="eps" data-cy="eps" type="text" />
              <ValidatedField id="ticker-tickerSymbol" name="tickerSymbolId" data-cy="tickerSymbol" label="Ticker Symbol" type="select">
                <option value="" key="0" />
                {tickerProfiles
                  ? tickerProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ticker" replace color="info">
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

export default TickerUpdate;
