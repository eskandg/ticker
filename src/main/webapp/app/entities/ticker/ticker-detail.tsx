import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './ticker.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TickerDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const tickerEntity = useAppSelector(state => state.ticker.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tickerDetailsHeading">Ticker</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{tickerEntity.id}</dd>
          <dt>
            <span id="symbol">Symbol</span>
          </dt>
          <dd>{tickerEntity.symbol}</dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>{tickerEntity.updatedAt ? <TextFormat value={tickerEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="priceChange">Price Change</span>
          </dt>
          <dd>{tickerEntity.priceChange}</dd>
          <dt>
            <span id="pricePercentChange">Price Percent Change</span>
          </dt>
          <dd>{tickerEntity.pricePercentChange}</dd>
          <dt>
            <span id="marketPrice">Market Price</span>
          </dt>
          <dd>{tickerEntity.marketPrice}</dd>
          <dt>
            <span id="marketCap">Market Cap</span>
          </dt>
          <dd>{tickerEntity.marketCap}</dd>
          <dt>
            <span id="volume">Volume</span>
          </dt>
          <dd>{tickerEntity.volume}</dd>
          <dt>
            <span id="avgVolume">Avg Volume</span>
          </dt>
          <dd>{tickerEntity.avgVolume}</dd>
          <dt>
            <span id="low">Low</span>
          </dt>
          <dd>{tickerEntity.low}</dd>
          <dt>
            <span id="high">High</span>
          </dt>
          <dd>{tickerEntity.high}</dd>
          <dt>
            <span id="open">Open</span>
          </dt>
          <dd>{tickerEntity.open}</dd>
          <dt>
            <span id="close">Close</span>
          </dt>
          <dd>{tickerEntity.close}</dd>
          <dt>
            <span id="previousClose">Previous Close</span>
          </dt>
          <dd>{tickerEntity.previousClose}</dd>
          <dt>
            <span id="bid">Bid</span>
          </dt>
          <dd>{tickerEntity.bid}</dd>
          <dt>
            <span id="ask">Ask</span>
          </dt>
          <dd>{tickerEntity.ask}</dd>
          <dt>
            <span id="bidVol">Bid Vol</span>
          </dt>
          <dd>{tickerEntity.bidVol}</dd>
          <dt>
            <span id="askVol">Ask Vol</span>
          </dt>
          <dd>{tickerEntity.askVol}</dd>
          <dt>
            <span id="fiftyTwoWeekLow">Fifty Two Week Low</span>
          </dt>
          <dd>{tickerEntity.fiftyTwoWeekLow}</dd>
          <dt>
            <span id="fiftyTwoWeekHigh">Fifty Two Week High</span>
          </dt>
          <dd>{tickerEntity.fiftyTwoWeekHigh}</dd>
          <dt>
            <span id="beta">Beta</span>
          </dt>
          <dd>{tickerEntity.beta}</dd>
          <dt>
            <span id="peRatio">Pe Ratio</span>
          </dt>
          <dd>{tickerEntity.peRatio}</dd>
          <dt>
            <span id="eps">Eps</span>
          </dt>
          <dd>{tickerEntity.eps}</dd>
          <dt>Ticker Symbol</dt>
          <dd>{tickerEntity.tickerSymbol ? tickerEntity.tickerSymbol.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ticker" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ticker/${tickerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TickerDetail;
