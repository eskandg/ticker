"""

[ Description ]

    This file is currently used to send ticker profile data to our database used by the Spring server (using tickers.csv for symbols)
    fetched from the yfinance API.

    It is only supposed to be run occasionally since ticker profile data does not change very much.
    Expect this to be removed and converted to Java later on.

"""


import requests
from csv import reader
from datetime import datetime as dt
from ballpark import business
from ballpark.notation import SI
import yfinance as yf

SI[9] = "B"  # changed from "G"
port = sys.argv[1]
auth_key = sys.argv[2]

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

# sends json to our server
def send_json(json_data, entity_path):
    request = requests.post(f'http://localhost:{port}/api/{entity_path}/?format=json',
                            headers={
                                "Authorization": auth_key,
                                "Accept": "application/json",
                                "Content-Type": "application/json"
                            },
                            json=json_data
                            )
    return request

tickers = []
csv_file = "tickers.csv" # a file containing ticker symbols
with open(csv_file, "r") as fd:
    reader = reader(fd)
    if next(reader):  # get rid of header
        for row in reader:
            tickers.append(row[0])

json_data = yf.Tickers(" ".join(tickers)).tickers  # GET request to yfinance with all our tickers
for ticker in json_data.values():
    profile_json = filter_ticker_profile_json(ticker.info)
    print(send_json(profile_json, "ticker-profiles").json())
