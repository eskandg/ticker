#!/usr/bin/env/python

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
    async with websockets.connect(uri, close_timeout=None) as websocket:
        sockets["finnhub_ws"] = websocket

        print("Finnhub client\n--------------")
        # print("\rWaiting for a connection", end="\r")

        while True:
            try:
                # receives subscribe data from our server to send to the finnhub server and receives ticker data from the finnhub server
                data = await websocket.recv()
            except:
                # print("Connection Lost. Reconnecting...")

                # websocket = await websockets.connect(uri)
                continue

            for connection in sockets["connections"]:  # sends the data retrieved from finnhub to all the clients
                try:
                    await connection.send(data)
                except websockets.ConnectionClosed:
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
            data = await websocket.recv()  # wait for subscription data
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
start_server = websockets.serve(ws_connection, "localhost", port)

asyncio.get_event_loop().run_until_complete(start_server)
asyncio.get_event_loop().run_until_complete(finnhub_client())
asyncio.get_event_loop().run_forever()
