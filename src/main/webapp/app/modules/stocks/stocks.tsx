import React, { useEffect, useState } from 'react';
import { useAppSelector, useAppDispatch } from 'app/config/store';
import { getEntities as getTickers } from '../../entities/ticker/ticker.reducer';
import { getProfiles, getEntity as getTickerProfile } from '../../entities/ticker-profile/ticker-profile.reducer';
import { ITicker } from 'app/shared/model/ticker.model';
import { Card, CardBody, CardImg, CardText, CardTitle, Col, Row } from 'reactstrap';
import { Ticker } from './Ticker/Ticker';

export const Stocks = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const tickers = useAppSelector(state => state.ticker.entities);
  const profiles = useAppSelector(state => state.tickerProfile.entities);
  const [deDupedTickers, setTickers] = useState([]);

  useEffect(() => {
    dispatch(getTickers({}));
    dispatch(getProfiles({}));
  }, []);

  return (
    <Row>
      {tickers.map(ticker => (
        <Ticker key={ticker.id} ticker={ticker} profileLogo={profiles.find(x => x.tickerSymbol === ticker.symbol)?.logoUrl} />
      ))}
      {/*<Ticker ticker={ticker} profileLogo={profileLogo}/>*/}
      {/*<Ticker ticker={ticker} profileLogo={profileLogo}/>*/}
      {/*<Ticker ticker={ticker} profileLogo={profileLogo}/>*/}
      {/*<Ticker ticker={ticker} profileLogo={profileLogo}/>*/}
      {/*<Ticker ticker={ticker} profileLogo={profileLogo}/>*/}

      {/*<Col md="4">*/}
      {/*  <Card style={{ width: 'min-content' }}>*/}
      {/*    /!*<CardImg src={profileLogo} style={{ height: '400px', width: 'auto' }} alt="Company Logo" thumbnail />*!/*/}
      {/*    <CardBody>*/}
      {/*      <CardTitle>{ticker.symbol}</CardTitle>*/}
      {/*      <CardText>*/}
      {/*        {ticker.pricePercentChange}*/}
      {/*        {ticker.priceChange}*/}
      {/*      </CardText>*/}
      {/*    </CardBody>*/}
      {/*  </Card>*/}
      {/*</Col>*/}
      {/*<Col>*/}
      {/*  <Card style={{ width: 'min-content' }}>*/}
      {/*    <CardImg src={profileLogo} style={{ height: '400px', width: 'auto' }} alt="Company Logo" thumbnail />*/}
      {/*    <CardBody>*/}
      {/*      <CardTitle>{ticker.symbol}</CardTitle>*/}
      {/*      <CardText>*/}
      {/*        {ticker.pricePercentChange}*/}
      {/*        {ticker.priceChange}*/}
      {/*      </CardText>*/}
      {/*    </CardBody>*/}
      {/*  </Card>*/}
      {/*</Col>*/}
      {/*<Col>*/}
      {/*  <Card style={{ width: 'min-content' }}>*/}
      {/*    <CardImg src={profileLogo} style={{ height: '400px', width: 'auto' }} alt="Company Logo" thumbnail />*/}
      {/*    <CardBody>*/}
      {/*      <CardTitle>{ticker.symbol}</CardTitle>*/}
      {/*      <CardText>*/}
      {/*        {ticker.pricePercentChange}*/}
      {/*        {ticker.priceChange}*/}
      {/*      </CardText>*/}
      {/*    </CardBody>*/}
      {/*  </Card>*/}
      {/*</Col>*/}
    </Row>
  );
};

export default Stocks;
