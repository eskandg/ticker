package com.ticker.repository;

import com.ticker.domain.TickerProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TickerProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TickerProfileRepository extends JpaRepository<TickerProfile, Long> {}
