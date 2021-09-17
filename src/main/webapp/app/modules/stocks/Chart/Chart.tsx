import React, { useEffect } from 'react';
import { MACDSeries } from 'react-financial-charts';
import { log } from 'react-jhipster';

export const Chart = ({ chartData }) => {
  // const data = {
  //   width: 500,
  //   height: 750,
  //   margins: {top: 20, right: 10, bottom: 0, left: 10},
  //   // lineClass: 'lineChart',
  //   dataset: chartData,
  // };

  return <div>{chartData.length > 0 && <MACDSeries yAccessor={chartData} />}</div>;
};

export default Chart;
