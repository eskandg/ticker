package com.ticker.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ticker.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TickerProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TickerProfile.class);
        TickerProfile tickerProfile1 = new TickerProfile();
        tickerProfile1.setId(1L);
        TickerProfile tickerProfile2 = new TickerProfile();
        tickerProfile2.setId(tickerProfile1.getId());
        assertThat(tickerProfile1).isEqualTo(tickerProfile2);
        tickerProfile2.setId(2L);
        assertThat(tickerProfile1).isNotEqualTo(tickerProfile2);
        tickerProfile1.setId(null);
        assertThat(tickerProfile1).isNotEqualTo(tickerProfile2);
    }
}
