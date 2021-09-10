import requests
from csv import reader
from datetime import datetime as dt
from ballpark import business
from ballpark.notation import SI
import yfinance as yf

SI[9] = "B"  # changed from "G"
auth_key = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTYzMTIzMjc5OH0.MXntfd294LhxVSJS1PwmvNkavc8mKTEyCJQYanA2VKi6gmG33xJS3inAlsF3gL0Xhf2niOwqzq8QI1lc7nT24w"


def calculate_change(prev_close_price, current_price):
    price_change = current_price - prev_close_price
    try:
        percentage_price_change = (price_change / prev_close_price) * 100
    except ZeroDivisionError:
        percentage_price_change = 0
        print(price_change, prev_close_price)
    return {"percentage": f'{percentage_price_change: .2f}%', "price": f'{price_change: .2f}'}


def format_date(date, timezone):
    return f'{date.strftime("%Y")}-{date.strftime("%m")}-{date.strftime("%d")}T{date.strftime("%X")}{timezone}'


def filter_ticker_json(data):
    time = dt.now()
    change = calculate_change(data.get("previousClose", 0), data.get("currentPrice", 0))
    filtered_json = {"updatedAt": format_date(time, "+05:00"),
                     "symbol": data.get("symbol"),
                     "priceChange": change.get("price"),
                     "pricePercentChange": change.get("percentage"),
                     "marketPrice": data.get("currentPrice"),
                     "marketCap": business(data.get("marketCap", 0), prefixes=SI, precision=3),
                     "volume": data.get("volume", 0),
                     "avgVolume": data.get("averageVolume", 0),
                     "low": data.get("dayLow", 0),
                     "high": data.get("dayHigh", 0),
                     "open": data.get("open", 0),
                     "close": data.get("open", 0),
                     "previousClose": data.get("previousClose", 0),
                     "bid": data.get("bid", 0),
                     "ask": data.get("ask", 0),
                     "bidVol": data.get("bidSize", 0),
                     "askVol": data.get("askSize", 0),
                     "fiftyTwoWeekLow": data.get("fiftyTwoWeekLow", 0),
                     "fiftyTwoWeekHigh": data.get("fiftyTwoWeekHigh", 0),
                     "beta": data.get("beta", 0),
                     "peRatio": data.get("trailingPE", 0),
                     "eps": data.get("trailingEps", 0),
                     }
    return filtered_json


def filter_ticker_profile_json(data):
    filtered_json = {
        "tickerSymbol": data.get("symbol"),
        "logoUrl": data.get("logo_url", ""),
        "industry": data.get("sector", "N/A"),
        "name": data.get("longName"),
        "phone": data.get("phone", "N/A"),
        "website": data.get("website", ""),
        "description": data.get("longBusinessSummary", "Not Available"),
        "fullTimeEmployees": data.get("fullTimeEmployees", 0),
    }
    return filtered_json


def send_json(json_data, entity_path):
    request = requests.post(f'http://localhost:9060/api/{entity_path}/?format=json',
                            headers={
                                "Authorization": auth_key,
                                "Accept": "application/json",
                                "Content-Type": "application/json"
                            },
                            json=json_data
                            )
    return request

tickers = []
csv_file = "tickers.csv"
with open(csv_file, "r") as fd:
    reader = reader(fd)
    if next(reader):  # get rid of header
        for row in reader:
            tickers.append(row[0])

json_data = yf.Tickers(" ".join(tickers)).tickers
for ticker in json_data.values():
    ticker_json = filter_ticker_json(ticker.info)
    profile_json = filter_ticker_profile_json(ticker.info)
    # print(send_json(ticker_json, "tickers").json())    
    print(send_json(profile_json, "ticker-profiles").json())
