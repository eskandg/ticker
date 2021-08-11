package com.ticker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
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

    @ManyToOne
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_watch_list__ticker",
        joinColumns = @JoinColumn(name = "watch_list_id"),
        inverseJoinColumns = @JoinColumn(name = "ticker_id")
    )
    @JsonIgnoreProperties(value = { "tickerSymbol", "watchedIns" }, allowSetters = true)
    private Set<Ticker> tickers = new HashSet<>();

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

    public Set<Ticker> getTickers() {
        return this.tickers;
    }

    public WatchList tickers(Set<Ticker> tickers) {
        this.setTickers(tickers);
        return this;
    }

    public WatchList addTicker(Ticker ticker) {
        this.tickers.add(ticker);
        ticker.getWatchedIns().add(this);
        return this;
    }

    public WatchList removeTicker(Ticker ticker) {
        this.tickers.remove(ticker);
        ticker.getWatchedIns().remove(this);
        return this;
    }

    public void setTickers(Set<Ticker> tickers) {
        this.tickers = tickers;
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
            "}";
    }
}
