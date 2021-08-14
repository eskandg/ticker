import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './ticker.reducer';
import { ITicker } from 'app/shared/model/ticker.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Ticker = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const tickerList = useAppSelector(state => state.ticker.entities);
  const loading = useAppSelector(state => state.ticker.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="ticker-heading" data-cy="TickerHeading">
        Tickers
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Ticker
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {tickerList && tickerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Symbol</th>
                <th>Updated At</th>
                <th>Price Change</th>
                <th>Price Percent Change</th>
                <th>Market Price</th>
                <th>Market Cap</th>
                <th>Volume</th>
                <th>Avg Volume</th>
                <th>Low</th>
                <th>High</th>
                <th>Open</th>
                <th>Close</th>
                <th>Previous Close</th>
                <th>Bid</th>
                <th>Ask</th>
                <th>Bid Vol</th>
                <th>Ask Vol</th>
                <th>Fifty Two Week Low</th>
                <th>Fifty Two Week High</th>
                <th>Beta</th>
                <th>Pe Ratio</th>
                <th>Eps</th>
                <th>Ticker Symbol</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {tickerList.map((ticker, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${ticker.id}`} color="link" size="sm">
                      {ticker.id}
                    </Button>
                  </td>
                  <td>{ticker.symbol}</td>
                  <td>{ticker.updatedAt ? <TextFormat type="date" value={ticker.updatedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{ticker.priceChange}</td>
                  <td>{ticker.pricePercentChange}</td>
                  <td>{ticker.marketPrice}</td>
                  <td>{ticker.marketCap}</td>
                  <td>{ticker.volume}</td>
                  <td>{ticker.avgVolume}</td>
                  <td>{ticker.low}</td>
                  <td>{ticker.high}</td>
                  <td>{ticker.open}</td>
                  <td>{ticker.close}</td>
                  <td>{ticker.previousClose}</td>
                  <td>{ticker.bid}</td>
                  <td>{ticker.ask}</td>
                  <td>{ticker.bidVol}</td>
                  <td>{ticker.askVol}</td>
                  <td>{ticker.fiftyTwoWeekLow}</td>
                  <td>{ticker.fiftyTwoWeekHigh}</td>
                  <td>{ticker.beta}</td>
                  <td>{ticker.peRatio}</td>
                  <td>{ticker.eps}</td>
                  <td>
                    {ticker.tickerSymbol ? <Link to={`ticker-profile/${ticker.tickerSymbol.id}`}>{ticker.tickerSymbol.id}</Link> : ''}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${ticker.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${ticker.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${ticker.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Tickers found</div>
        )}
      </div>
    </div>
  );
};

export default Ticker;
