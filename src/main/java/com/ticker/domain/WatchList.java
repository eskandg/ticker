package com.ticker.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WatchList.
 */
@Entity
@Table(name = "watch_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WatchList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "ticker_symbol", nullable = false)
    private String tickerSymbol;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WatchList id(Long id) {
        this.id = id;
        return this;
    }

    public String getTickerSymbol() {
        return this.tickerSymbol;
    }

    public WatchList tickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
        return this;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public User getUser() {
        return this.user;
    }

    public WatchList user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WatchList)) {
            return false;
        }
        return id != null && id.equals(((WatchList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WatchList{" +
            "id=" + getId() +
            ", tickerSymbol='" + getTickerSymbol() + "'" +
            "}";
    }
}
