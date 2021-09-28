import React from 'react';
import { Card, CardBody, CardImg, CardText, CardTitle, Col } from 'reactstrap';
import { Link } from 'react-router-dom';
import TickerFollowButton from 'app/modules/stocks/WatchList/TickerFollowButton';
import { log } from 'react-jhipster';

export const Ticker = ({ ticker }) => {
  return (
    <Col className="mx-xs-auto mx-sm-auto mx-xl-0" xs="12" sm="12" md="8" lg="6" xl="3">
      <Card className="mb-5">
        <TickerFollowButton
          key={ticker['tickerSymbol']}
          tickerSymbol={ticker['tickerSymbol']}
          style={{ placeSelf: 'self-end' }}
          usage="stocks"
        />
        <CardBody>
          <Link to={`/profile/${ticker['tickerSymbol']}`}>
            <CardTitle>{ticker['tickerSymbol']}</CardTitle>
            <CardText>
              {ticker && ticker['price'].toFixed(2)}
              <br />
              {ticker && ticker['change'].toFixed(2)}
              <br />
              {ticker && ticker['percentChange'].toFixed(3)}%
            </CardText>
          </Link>
        </CardBody>
      </Card>
    </Col>
  );
};

export default Ticker;
