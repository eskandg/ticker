import React from 'react';
import { Card, CardBody, CardImg, CardText, CardTitle, Col } from 'reactstrap';
import { log } from 'react-jhipster';
import TickerFollowButton from 'app/modules/stocks/WatchList/TickerFollowButton';
import millify from 'millify';

export const TickerProfile = ({ ticker, profileData }) => {
  return (
    <Col className="mx-xs-auto mx-sm-auto mx-xl-0" xs="12" sm="12" md="12" lg="12" xl="12">
      <Card className="mb-5">
        {ticker && (
          <TickerFollowButton
            key={ticker['tickerSymbol']}
            style={{ placeSelf: 'self-end' }}
            tickerSymbol={ticker['tickerSymbol']}
            usage="profile"
          />
        )}
        <CardBody>
          <CardTitle>
            {ticker && ticker['tickerSymbol']}
            <br />
            {profileData['name']}
            <br />
            {profileData['industry']}
          </CardTitle>
          <CardText>
            <p>
              {ticker && ticker['price'].toFixed(2)} {ticker && ticker['change'].toFixed(2)}
              <br />
              {ticker && ticker['percentChange'].toFixed(3)}%
            </p>
            <hr />
            <p>
              Market Cap: {ticker && millify(ticker['marketCap'])}
              <br />
              Phone: {profileData['phone']}
              <br />
              Website: {profileData['website']}
              <br />
              Number of Employees: {profileData['fullTimeEmployees']}
              <br />
            </p>
            <hr />
            <p>
              Description: <br /> {profileData['description']}
            </p>
          </CardText>
        </CardBody>
      </Card>
    </Col>
  );
};

export default TickerProfile;
