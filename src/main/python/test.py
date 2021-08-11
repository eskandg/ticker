import requests
from datetime import datetime as dt
import yfinance as yf

json_test = yf.Ticker("AAPL").info


def filter_json(data):
    time = dt.now()
    test = {"updatedAt": f'{time.strftime("%Y")}-{time.strftime("%m")}-{time.strftime("%d")}T{time.strftime("%X")}+05:00',
            "symbol": data["symbol"],
            "change": 1.0,
            "marketPrice": data["currentPrice"],
            "marketCap": str(data["marketCap"]),
            "volume": data["volume"],
            "avgVolume": data["averageVolume"],
            "low": 1.0,
            "high": 1.0,
            "open": data["open"],
            "close": data["open"],
            "previousClose": data["previousClose"],
            "bid": data["bid"],
            "ask": data["ask"],
            "bidVol": data["bidSize"],
            "askVol": data["askSize"],
            "dayLow": data["dayLow"],
            "dayHigh": data["dayHigh"],
            "fiftyTwoWeekLow": data["fiftyTwoWeekLow"],
            "fiftyTwoWeekHigh": data["fiftyTwoWeekHigh"],
            "beta": data["beta"],
            "peRatio": data["trailingPE"],
            "eps": data["trailingEps"],
            }
    return test


ticker = filter_json(json_test)
test = requests.post("http://localhost:9060/api/tickers/?format=json",
                     headers={
                         "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTYzMTIzMjc5OH0.MXntfd294LhxVSJS1PwmvNkavc8mKTEyCJQYanA2VKi6gmG33xJS3inAlsF3gL0Xhf2niOwqzq8QI1lc7nT24w",
                         'Accept': 'application/json',
                         'Content-Type': 'application/json'
                     },
                     json=ticker)
print(test.json())
