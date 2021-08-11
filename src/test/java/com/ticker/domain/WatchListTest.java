package com.ticker.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ticker.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WatchListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WatchList.class);
        WatchList watchList1 = new WatchList();
        watchList1.setId(1L);
        WatchList watchList2 = new WatchList();
        watchList2.setId(watchList1.getId());
        assertThat(watchList1).isEqualTo(watchList2);
        watchList2.setId(2L);
        assertThat(watchList1).isNotEqualTo(watchList2);
        watchList1.setId(null);
        assertThat(watchList1).isNotEqualTo(watchList2);
    }
}
