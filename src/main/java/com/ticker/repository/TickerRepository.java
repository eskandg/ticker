package com.ticker.repository;

import com.ticker.domain.Ticker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ticker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TickerRepository extends JpaRepository<Ticker, Long> {
    Optional<Ticker> findFirstBySymbolOrderByUpdatedAtDesc(String symbol);
}
