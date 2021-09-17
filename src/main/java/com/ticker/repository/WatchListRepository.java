package com.ticker.repository;

import com.ticker.domain.User;
import com.ticker.domain.WatchList;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WatchList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WatchListRepository extends JpaRepository<WatchList, Long> {
    @Query("select watchList from WatchList watchList where watchList.user.login = ?#{principal.username}")
    List<WatchList> findByUserIsCurrentUser();

    Optional<List<WatchList>> findAllByUser(User user);
    Optional<WatchList> findFirstByUserAndTickerSymbol(User user, @NotNull String tickerSymbol);
    Optional<WatchList> deleteAllByUserAndTickerSymbol(User user, @NotNull String tickerSymbol);
}
