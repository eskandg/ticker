package com.ticker.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ticker.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TickerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ticker.class);
        Ticker ticker1 = new Ticker();
        ticker1.setId(1L);
        Ticker ticker2 = new Ticker();
        ticker2.setId(ticker1.getId());
        assertThat(ticker1).isEqualTo(ticker2);
        ticker2.setId(2L);
        assertThat(ticker1).isNotEqualTo(ticker2);
        ticker1.setId(null);
        assertThat(ticker1).isNotEqualTo(ticker2);
    }
}
