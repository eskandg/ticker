package com.ticker.repository;

import com.ticker.domain.WatchList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WatchList entity.
 */
@Repository
public interface WatchListRepository extends JpaRepository<WatchList, Long> {
    @Query("select watchList from WatchList watchList where watchList.user.login = ?#{principal.username}")
    List<WatchList> findByUserIsCurrentUser();

    @Query(
        value = "select distinct watchList from WatchList watchList left join fetch watchList.tickers",
        countQuery = "select count(distinct watchList) from WatchList watchList"
    )
    Page<WatchList> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct watchList from WatchList watchList left join fetch watchList.tickers")
    List<WatchList> findAllWithEagerRelationships();

    @Query("select watchList from WatchList watchList left join fetch watchList.tickers where watchList.id =:id")
    Optional<WatchList> findOneWithEagerRelationships(@Param("id") Long id);
}
