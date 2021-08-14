import requests
from datetime import datetime as dt
from ballpark import business
from ballpark.notation import SI
import yfinance as yf

SI[9] = "B"  # changed from "G"
auth_key = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTYzMTIzMjc5OH0.MXntfd294LhxVSJS1PwmvNkavc8mKTEyCJQYanA2VKi6gmG33xJS3inAlsF3gL0Xhf2niOwqzq8QI1lc7nT24w"


def calculate_change(prev_close_price, current_price):
    price_change = current_price - prev_close_price
    percentage_price_change = (price_change / prev_close_price) * 100
    return {"percentage": f'{percentage_price_change: .2f}%', "price": f'{price_change: .2f}'}


def format_date(date, timezone):
    return f'{date.strftime("%Y")}-{date.strftime("%m")}-{date.strftime("%d")}T{date.strftime("%X")}{timezone}'


def filter_ticker_json(data):
    time = dt.now()
    change = calculate_change(data["previousClose"], data["currentPrice"])
    filtered_json = {"updatedAt": format_date(time, "+05:00"),
                     "symbol": data["symbol"],
                     "priceChange": change["price"],
                     "pricePercentChange": change["percentage"],
                     "marketPrice": data["currentPrice"],
                     "marketCap": business(data["marketCap"], prefixes=SI, precision=3),
                     "volume": data["volume"],
                     "avgVolume": data["averageVolume"],
                     "low": data["dayLow"],
                     "high": data["dayHigh"],
                     "open": data["open"],
                     "close": data["open"],
                     "previousClose": data["previousClose"],
                     "bid": data["bid"],
                     "ask": data["ask"],
                     "bidVol": data["bidSize"],
                     "askVol": data["askSize"],
                     "fiftyTwoWeekLow": data["fiftyTwoWeekLow"],
                     "fiftyTwoWeekHigh": data["fiftyTwoWeekHigh"],
                     "beta": data["beta"],
                     "peRatio": data["trailingPE"],
                     "eps": data["trailingEps"],
                     }
    return filtered_json


def filter_ticker_profile_json(data):
    filtered_json = {
        "tickerSymbol": data["symbol"],
        "logoUrl": data["logo_url"],
        "industry": data["sector"],
        "name": data["longName"],
        "phone": data["phone"],
        "website": data["website"],
        "description": data["longBusinessSummary"],
        "fullTimeEmployees": data["fullTimeEmployees"],
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

json_data = yf.Ticker("MSFT").info
ticker_json = filter_ticker_json(json_data)
profile_json = filter_ticker_profile_json(json_data)
print(send_json(ticker_json, "tickers").json())
