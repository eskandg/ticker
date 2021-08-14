package com.ticker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ticker.
 */
@Entity
@Table(name = "ticker")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ticker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "price_change")
    private String priceChange;

    @Column(name = "price_percent_change")
    private String pricePercentChange;

    @Column(name = "market_price")
    private Double marketPrice;

    @Column(name = "market_cap")
    private String marketCap;

    @Column(name = "volume")
    private Integer volume;

    @Column(name = "avg_volume")
    private Integer avgVolume;

    @Column(name = "low")
    private Double low;

    @Column(name = "high")
    private Double high;

    @Column(name = "open")
    private Double open;

    @Column(name = "close")
    private Double close;

    @Column(name = "previous_close")
    private Double previousClose;

    @Column(name = "bid")
    private Double bid;

    @Column(name = "ask")
    private Double ask;

    @Column(name = "bid_vol")
    private Double bidVol;

    @Column(name = "ask_vol")
    private Double askVol;

    @Column(name = "fifty_two_week_low")
    private Double fiftyTwoWeekLow;

    @Column(name = "fifty_two_week_high")
    private Double fiftyTwoWeekHigh;

    @Column(name = "beta")
    private Float beta;

    @Column(name = "pe_ratio")
    private Float peRatio;

    @Column(name = "eps")
    private Float eps;

    @JsonIgnoreProperties(value = { "symbol" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private TickerProfile tickerSymbol;

    @ManyToMany(mappedBy = "tickers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "tickers" }, allowSetters = true)
    private Set<WatchList> watchedIns = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ticker id(Long id) {
        this.id = id;
        return this;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public Ticker symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Ticker updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPriceChange() {
        return this.priceChange;
    }

    public Ticker priceChange(String priceChange) {
        this.priceChange = priceChange;
        return this;
    }

    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }

    public String getPricePercentChange() {
        return this.pricePercentChange;
    }

    public Ticker pricePercentChange(String pricePercentChange) {
        this.pricePercentChange = pricePercentChange;
        return this;
    }

    public void setPricePercentChange(String pricePercentChange) {
        this.pricePercentChange = pricePercentChange;
    }

    public Double getMarketPrice() {
        return this.marketPrice;
    }

    public Ticker marketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
        return this;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getMarketCap() {
        return this.marketCap;
    }

    public Ticker marketCap(String marketCap) {
        this.marketCap = marketCap;
        return this;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public Integer getVolume() {
        return this.volume;
    }

    public Ticker volume(Integer volume) {
        this.volume = volume;
        return this;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getAvgVolume() {
        return this.avgVolume;
    }

    public Ticker avgVolume(Integer avgVolume) {
        this.avgVolume = avgVolume;
        return this;
    }

    public void setAvgVolume(Integer avgVolume) {
        this.avgVolume = avgVolume;
    }

    public Double getLow() {
        return this.low;
    }

    public Ticker low(Double low) {
        this.low = low;
        return this;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getHigh() {
        return this.high;
    }

    public Ticker high(Double high) {
        this.high = high;
        return this;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getOpen() {
        return this.open;
    }

    public Ticker open(Double open) {
        this.open = open;
        return this;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getClose() {
        return this.close;
    }

    public Ticker close(Double close) {
        this.close = close;
        return this;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getPreviousClose() {
        return this.previousClose;
    }

    public Ticker previousClose(Double previousClose) {
        this.previousClose = previousClose;
        return this;
    }

    public void setPreviousClose(Double previousClose) {
        this.previousClose = previousClose;
    }

    public Double getBid() {
        return this.bid;
    }

    public Ticker bid(Double bid) {
        this.bid = bid;
        return this;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public Double getAsk() {
        return this.ask;
    }

    public Ticker ask(Double ask) {
        this.ask = ask;
        return this;
    }

    public void setAsk(Double ask) {
        this.ask = ask;
    }

    public Double getBidVol() {
        return this.bidVol;
    }

    public Ticker bidVol(Double bidVol) {
        this.bidVol = bidVol;
        return this;
    }

    public void setBidVol(Double bidVol) {
        this.bidVol = bidVol;
    }

    public Double getAskVol() {
        return this.askVol;
    }

    public Ticker askVol(Double askVol) {
        this.askVol = askVol;
        return this;
    }

    public void setAskVol(Double askVol) {
        this.askVol = askVol;
    }

    public Double getFiftyTwoWeekLow() {
        return this.fiftyTwoWeekLow;
    }

    public Ticker fiftyTwoWeekLow(Double fiftyTwoWeekLow) {
        this.fiftyTwoWeekLow = fiftyTwoWeekLow;
        return this;
    }

    public void setFiftyTwoWeekLow(Double fiftyTwoWeekLow) {
        this.fiftyTwoWeekLow = fiftyTwoWeekLow;
    }

    public Double getFiftyTwoWeekHigh() {
        return this.fiftyTwoWeekHigh;
    }

    public Ticker fiftyTwoWeekHigh(Double fiftyTwoWeekHigh) {
        this.fiftyTwoWeekHigh = fiftyTwoWeekHigh;
        return this;
    }

    public void setFiftyTwoWeekHigh(Double fiftyTwoWeekHigh) {
        this.fiftyTwoWeekHigh = fiftyTwoWeekHigh;
    }

    public Float getBeta() {
        return this.beta;
    }

    public Ticker beta(Float beta) {
        this.beta = beta;
        return this;
    }

    public void setBeta(Float beta) {
        this.beta = beta;
    }

    public Float getPeRatio() {
        return this.peRatio;
    }

    public Ticker peRatio(Float peRatio) {
        this.peRatio = peRatio;
        return this;
    }

    public void setPeRatio(Float peRatio) {
        this.peRatio = peRatio;
    }

    public Float getEps() {
        return this.eps;
    }

    public Ticker eps(Float eps) {
        this.eps = eps;
        return this;
    }

    public void setEps(Float eps) {
        this.eps = eps;
    }

    public TickerProfile getTickerSymbol() {
        return this.tickerSymbol;
    }

    public Ticker tickerSymbol(TickerProfile tickerProfile) {
        this.setTickerSymbol(tickerProfile);
        return this;
    }

    public void setTickerSymbol(TickerProfile tickerProfile) {
        this.tickerSymbol = tickerProfile;
    }

    public Set<WatchList> getWatchedIns() {
        return this.watchedIns;
    }

    public Ticker watchedIns(Set<WatchList> watchLists) {
        this.setWatchedIns(watchLists);
        return this;
    }

    public Ticker addWatchedIn(WatchList watchList) {
        this.watchedIns.add(watchList);
        watchList.getTickers().add(this);
        return this;
    }

    public Ticker removeWatchedIn(WatchList watchList) {
        this.watchedIns.remove(watchList);
        watchList.getTickers().remove(this);
        return this;
    }

    public void setWatchedIns(Set<WatchList> watchLists) {
        if (this.watchedIns != null) {
            this.watchedIns.forEach(i -> i.removeTicker(this));
        }
        if (watchLists != null) {
            watchLists.forEach(i -> i.addTicker(this));
        }
        this.watchedIns = watchLists;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticker)) {
            return false;
        }
        return id != null && id.equals(((Ticker) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ticker{" +
            "id=" + getId() +
            ", symbol='" + getSymbol() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", priceChange='" + getPriceChange() + "'" +
            ", pricePercentChange='" + getPricePercentChange() + "'" +
            ", marketPrice=" + getMarketPrice() +
            ", marketCap='" + getMarketCap() + "'" +
            ", volume=" + getVolume() +
            ", avgVolume=" + getAvgVolume() +
            ", low=" + getLow() +
            ", high=" + getHigh() +
            ", open=" + getOpen() +
            ", close=" + getClose() +
            ", previousClose=" + getPreviousClose() +
            ", bid=" + getBid() +
            ", ask=" + getAsk() +
            ", bidVol=" + getBidVol() +
            ", askVol=" + getAskVol() +
            ", fiftyTwoWeekLow=" + getFiftyTwoWeekLow() +
            ", fiftyTwoWeekHigh=" + getFiftyTwoWeekHigh() +
            ", beta=" + getBeta() +
            ", peRatio=" + getPeRatio() +
            ", eps=" + getEps() +
            "}";
    }
}
