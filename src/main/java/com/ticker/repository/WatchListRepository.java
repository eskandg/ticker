package com.ticker.repository;

import com.ticker.domain.WatchList;
import java.util.List;
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
}
