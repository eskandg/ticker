import React, { useEffect, useState } from 'react';
import { useAppSelector, useAppDispatch } from 'app/config/store';
import { getEntity as getTicker } from '../../entities/ticker/ticker.reducer';
import { getEntity as getTickerProfile } from '../../entities/ticker-profile/ticker-profile.reducer';
import { ITicker } from 'app/shared/model/ticker.model';
import { Card, CardBody, CardImg, CardText, CardTitle, Col, Row } from 'reactstrap';

export const Stocks = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const ticker = useAppSelector(state => state.ticker.entity);
  const profileLogo = useAppSelector(state => state.tickerProfile.entity.logoUrl);

  useEffect(() => {
    dispatch(getTicker('MSFT'));
    dispatch(getTickerProfile('MSFT'));
  }, []);

  return (
    <Row>
      <Col>
        <Card style={{ width: 'min-content' }}>
          <CardImg src={profileLogo} style={{ height: '400px', width: 'auto' }} alt="Company Logo" thumbnail />
          <CardBody>
            <CardTitle>{ticker.symbol}</CardTitle>
            <CardText>
              {ticker.pricePercentChange}
              {ticker.priceChange}
            </CardText>
          </CardBody>
        </Card>
      </Col>
    </Row>
  );
};

export default Stocks;
