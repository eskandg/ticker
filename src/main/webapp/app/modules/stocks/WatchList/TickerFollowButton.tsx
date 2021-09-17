import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button, Tooltip } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faRss } from '@fortawesome/free-solid-svg-icons';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createEntity as createWatchListElement, setUserFollows } from 'app/entities/watch-list/watch-list.reducer';
import { deleteEntity as deleteWatchListElement } from 'app/entities/watch-list/watch-list.reducer';
import { log } from 'react-jhipster';

export const TickerFollowButton = ({ className = '', tickerSymbol, style = {}, usage }) => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const userFollows = useAppSelector(state => state.watchList.userFollows);
  const [watchListElement, setWatchListElement] = useState();
  const [cssStyle, setCssStyle] = useState(style);
  const [useModal, setUseModal] = useState(false);
  const [useTooltip, setUseToolTip] = useState(false);

  const onColor = 'deepskyblue';
  const offColor = 'lightgray';

  let responseData;
  useEffect(() => {
    setCssStyle({ ...cssStyle, color: offColor });

    responseData = axios.get(`api/watch-lists/${account.login}/${tickerSymbol}`).then(jsonResponse => {
      if (jsonResponse && !(tickerSymbol in userFollows)) {
        dispatch(setUserFollows({ tickerSymbol: tickerSymbol, type: 'add' }));
      }
      return jsonResponse;
    });
  }, []);

  useEffect(() => {
    // log(userFollows);
    if (new Set(userFollows).has(tickerSymbol)) setCssStyle({ ...cssStyle, color: onColor });
    else setCssStyle({ ...cssStyle, color: offColor });
  }, [userFollows]);

  function handleOnClick() {
    if (new Set(userFollows).has(tickerSymbol)) {
      setUseModal(true);
    } else {
      const newWatchListElement = { tickerSymbol: tickerSymbol, user: account };
      dispatch(createWatchListElement(newWatchListElement));
      dispatch(setUserFollows({ tickerSymbol: tickerSymbol, type: 'add' }));
    }
  }

  function handleModalButton(buttonText) {
    setUseModal(!useModal);
    if (buttonText === 'Yes') {
      dispatch(deleteWatchListElement({ user: account.login, tickerSymbol: tickerSymbol }));
      dispatch(setUserFollows({ tickerSymbol: tickerSymbol, type: 'remove' }));
    }
  }

  function handleToolTip() {
    setUseToolTip(!useTooltip);
  }

  return (
    <>
      <span
        className={`${className}`}
        id={`followButtonToolTip-${tickerSymbol}-${usage}`}
        role="button"
        onClick={handleOnClick}
        style={cssStyle}
      >
        <FontAwesomeIcon icon={faRss} />
      </span>

      <Tooltip
        autohide={false}
        placement="top"
        isOpen={useTooltip}
        toggle={handleToolTip}
        target={`followButtonToolTip-${tickerSymbol}-${usage}`}
      >
        {new Set(userFollows).has(tickerSymbol) ? <p className="my-auto">Unfollow</p> : <p className="my-auto">Follow</p>}
      </Tooltip>

      {/* activated when useModal equals true */}
      <Modal className="modal-dialog-centered" isOpen={useModal} toggle={handleOnClick}>
        <ModalHeader>Unfollow '{tickerSymbol}'</ModalHeader>
        <ModalBody>Are you sure you want to unfollow '{tickerSymbol}'?</ModalBody>
        <ModalFooter>
          <Button onClick={e => handleModalButton(e.target.innerText)}>Yes</Button>
          <Button onClick={e => handleModalButton(e.target.innerText)}>No</Button>
        </ModalFooter>
      </Modal>
    </>
  );
};

export default TickerFollowButton;
