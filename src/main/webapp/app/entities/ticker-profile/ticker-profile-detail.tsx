import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './ticker-profile.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TickerProfileDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const tickerProfileEntity = useAppSelector(state => state.tickerProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tickerProfileDetailsHeading">TickerProfile</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{tickerProfileEntity.id}</dd>
          <dt>
            <span id="tickerSymbol">Ticker Symbol</span>
          </dt>
          <dd>{tickerProfileEntity.tickerSymbol}</dd>
          <dt>
            <span id="logoUrl">Logo Url</span>
          </dt>
          <dd>{tickerProfileEntity.logoUrl}</dd>
          <dt>
            <span id="industry">Industry</span>
          </dt>
          <dd>{tickerProfileEntity.industry}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{tickerProfileEntity.name}</dd>
          <dt>
            <span id="phone">Phone</span>
          </dt>
          <dd>{tickerProfileEntity.phone}</dd>
          <dt>
            <span id="website">Website</span>
          </dt>
          <dd>{tickerProfileEntity.website}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{tickerProfileEntity.description}</dd>
          <dt>
            <span id="fullTimeEmployees">Full Time Employees</span>
          </dt>
          <dd>{tickerProfileEntity.fullTimeEmployees}</dd>
        </dl>
        <Button tag={Link} to="/ticker-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ticker-profile/${tickerProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TickerProfileDetail;
