import React, { useEffect, useLayoutEffect } from 'react';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { logout } from 'app/shared/reducers/authentication';
import { clearUserWatchListState } from 'app/entities/watch-list/watch-list.reducer';

export const Logout = () => {
  const logoutUrl = useAppSelector(state => state.authentication.logoutUrl);
  const idToken = useAppSelector(state => state.authentication.idToken);
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(clearUserWatchListState());
  }, []);

  useLayoutEffect(() => {
    dispatch(logout());
    if (logoutUrl) {
      // if Keycloak, logoutUrl has protocol/openid-connect in it
      window.location.href = logoutUrl.includes('/protocol')
        ? logoutUrl + '?redirect_uri=' + window.location.origin
        : logoutUrl + '?id_token_hint=' + idToken + '&post_logout_redirect_uri=' + window.location.origin;
    }
  });

  return (
    <div className="p-5">
      <h4>Logged out successfully!</h4>
    </div>
  );
};

export default Logout;
