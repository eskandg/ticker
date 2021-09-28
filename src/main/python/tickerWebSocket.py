#!/usr/bin/env/python

"""

[ Description ]

    This creates our websocket server for ticker trade data from Finnhub.
    The purpose of doing this in the back-end and not the front-end is to prevent the API key from being exposed for security reasons.

    { How it works }
        The server takes incoming connections from clients on the front-end and receives ticker subscription data.
        This data is then sent to another websocket, the Finnhub client.
        The Finnhub client then waits for ticker data and sends the data to our server its also been waiting for.
        Our server then sends the data received to all of our connected clients.

"""


import json
import asyncio
import websockets
import sys
import logging

logger = logging.getLogger('websockets')
logger.setLevel(logging.DEBUG)
logger.addHandler(logging.StreamHandler())

sockets = {"finnhub_ws": None, "connections": []}

# used for clearing up subscriptions
user_subscriptions = {}
subscriptions_to_finnhub = {}

def show_available_connections():
    available_connections = len(sockets['connections'])
    if available_connections > 0:
        print(f"Available connections: {available_connections}", end="\r")
    else:
        print("\rWaiting for a connection", end="\r")


async def finnhub_client():
    api_key = sys.argv[1]
    uri = f"wss://ws.finnhub.io?token={api_key}"
    async with websockets.connect(uri, ping_interval=None, close_timeout=None) as websocket:
        sockets["finnhub_ws"] = websocket

        print("Finnhub client\n--------------")
        # print("\rWaiting for a connection", end="\r")

        while True:
            try:
                # receives subscribe data from our server to send to the finnhub server and receives ticker data from the finnhub server
                data = await websocket.recv()
                print("finnhub receive")
            except:
                # print("Connection Lost. Reconnecting...")

                # websocket = await websockets.connect(uri)
                continue

            for connection in sockets["connections"]:  # sends the data retrieved from finnhub to all the users
                print("sending data to connections")
                try:
                    await connection.send(data)
                except websockets.ConnectionClosed:  # reduce subscriptions made by the user
                    for subscription in user_subscriptions[connection]:
                        subscriptions_to_finnhub[subscription] -= 1
                        if subscriptions_to_finnhub[subscription] == 0:
                            await websocket.send(json.dumps({"type": "unsubscribe", "symbol": subscription}))

                    user_subscriptions.pop(connection)
                    sockets["connections"].remove(connection)

                    # show_available_connections()
                    continue


async def ws_connection(websocket, path):
    sockets["connections"].append(websocket)
    # show_available_connections()
    while True:
        if websocket.closed:  # client closes connection
            break

        if websocket not in user_subscriptions:
            user_subscriptions[websocket] = []

        try:
            data = await websocket.recv()  # wait for subscription data from the client on the front-end
        except websockets.ConnectionClosed:
            continue

        subscription = json.loads(data)["symbol"]
        user_subscriptions[websocket].append(subscription)

        if subscription not in subscriptions_to_finnhub:
            subscriptions_to_finnhub[subscription] = 1
        else:
            subscriptions_to_finnhub[subscription] += 1

        try:
            await sockets["finnhub_ws"].send(data)
        except AttributeError or websockets.ConnectionClosed:
            continue


port = 8000
start_server = websockets.serve(ws_connection, "localhost", port, ping_interval=None, close_timeout=None)

asyncio.get_event_loop().run_until_complete(start_server)
asyncio.get_event_loop().run_until_complete(finnhub_client())
asyncio.get_event_loop().run_forever()
