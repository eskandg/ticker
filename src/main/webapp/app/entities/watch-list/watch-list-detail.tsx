import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './watch-list.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WatchListDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const watchListEntity = useAppSelector(state => state.watchList.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="watchListDetailsHeading">WatchList</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{watchListEntity.id}</dd>
          <dt>
            <span id="tickerSymbol">Ticker Symbol</span>
          </dt>
          <dd>{watchListEntity.tickerSymbol}</dd>
          <dt>User</dt>
          <dd>{watchListEntity.user ? watchListEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/watch-list" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/watch-list/${watchListEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default WatchListDetail;
