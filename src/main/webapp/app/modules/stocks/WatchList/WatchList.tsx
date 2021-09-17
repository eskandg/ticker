import { useAppDispatch, useAppSelector } from 'app/config/store';
import React, { useEffect, useState } from 'react';
import { getEntitiesByUser } from 'app/entities/watch-list/watch-list.reducer';
import { Modal, ModalBody, ModalHeader, Tooltip } from 'reactstrap';
import { Link } from 'react-router-dom';
import TickerFollowButton from 'app/modules/stocks/WatchList/TickerFollowButton';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Fab } from 'react-tiny-fab';
import { addSymbol, getStock } from 'app/modules/stocks/stocks-reducer';
import { log } from 'react-jhipster';

export const WatchList = props => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const tickers = useAppSelector(state => state.ticker.entities);
  const watchListElements = useAppSelector(state => state.watchList.userFollows);
  const [useModal, setUseModal] = useState(false);
  const [useTooltip, setUseTooltip] = useState(false);

  useEffect(() => {
    if (account.login !== undefined) {
      dispatch(getEntitiesByUser(account.login));
    }
  }, [account.login]);

  useEffect(() => {
    watchListElements.map(tickerSymbol => {
      if (!(tickerSymbol in tickers)) {
        dispatch(addSymbol(tickerSymbol));
        dispatch(getStock({ symbol: tickerSymbol, getMoreDetails: true, isSocketActive: false }));
      }
    });
  }, [watchListElements]);

  const modalHandler = () => {
    setUseModal(!useModal);
  };

  const handleWatchListTooltip = () => {
    if (useModal) {
      setUseTooltip(false);
    } else setUseTooltip(!useTooltip);
  };

  return (
    <>
      <Modal scrollable={true} className="modal-dialog-centered" backdrop={true} isOpen={useModal} toggle={modalHandler}>
        <ModalHeader toggle={modalHandler}>Your Watchlist</ModalHeader>
        <ModalBody>
          {watchListElements.length > 0 ? (
            watchListElements.map((tickerSymbol, index, array) => (
              <div key={tickerSymbol}>
                <div>
                  <span>
                    <Link onClick={modalHandler} to={`/profile/${tickerSymbol}`}>
                      {tickerSymbol}
                    </Link>
                    <TickerFollowButton key={tickerSymbol} className="ml-3" tickerSymbol={tickerSymbol} usage="watchlist" />
                    <br />
                  </span>
                  <span>{tickers[tickerSymbol] && tickers[tickerSymbol].price.toFixed(2)}</span>
                  <br />
                  <span>{tickers[tickerSymbol] && tickers[tickerSymbol].change.toFixed(2)}</span>
                  <br />
                  <span>{tickers[tickerSymbol] && tickers[tickerSymbol].percentChange.toFixed(3)}</span>
                </div>
                {index + 1 !== array.length ? <hr /> : null} {/* no line at last element */}
              </div>
            ))
          ) : (
            <h3 className="text-center">Your watchlist is empty.</h3>
          )}
        </ModalBody>
      </Modal>

      <Fab id="watchlist-tooltip" icon={<FontAwesomeIcon icon="list" />} event="click" onClick={modalHandler} />

      <Tooltip isOpen={useTooltip} toggle={handleWatchListTooltip} placement="left" onClick={setUseTooltip} target="watchlist-tooltip">
        Watchlist
      </Tooltip>
    </>
  );
};
