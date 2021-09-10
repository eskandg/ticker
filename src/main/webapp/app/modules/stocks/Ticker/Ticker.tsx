import React from 'react';
import { Card, CardBody, CardImg, CardText, CardTitle, Col } from 'reactstrap';

export const Ticker = ({ ticker, profileLogo }) => {
  return (
    <Col className="mx-xs-auto mx-sm-auto mx-xl-0" xs="12" sm="12" md="8" lg="6" xl="3">
      <Card className="mb-5">
        <CardImg
          src={profileLogo}
          className="card-img-top img-thumbnail img-responsive img-fluid"
          style={{ height: '400px', width: 'auto' }}
          alt="Company Logo"
          thumbnail
        />
        <CardBody>
          <CardTitle>{ticker['tickerSymbol']}</CardTitle>
          <CardText>
            {ticker['price'].toFixed(2)}
            <br />
            {ticker['change'].toFixed(2)}
            <br />
            {ticker['percentChange'].toFixed(3)}%
          </CardText>
        </CardBody>
      </Card>
    </Col>
  );
};

export default Ticker;
