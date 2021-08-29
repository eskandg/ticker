package com.ticker.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A TickerProfile.
 */
@Entity
@Table(name = "ticker_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TickerProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "ticker_symbol", nullable = false, unique = true)
    private String tickerSymbol;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "industry")
    private String industry;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "website")
    private String website;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @Column(name = "full_time_employees")
    private Integer fullTimeEmployees;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TickerProfile id(Long id) {
        this.id = id;
        return this;
    }

    public String getTickerSymbol() {
        return this.tickerSymbol;
    }

    public TickerProfile tickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
        return this;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public TickerProfile logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getIndustry() {
        return this.industry;
    }

    public TickerProfile industry(String industry) {
        this.industry = industry;
        return this;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getName() {
        return this.name;
    }

    public TickerProfile name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public TickerProfile phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return this.website;
    }

    public TickerProfile website(String website) {
        this.website = website;
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return this.description;
    }

    public TickerProfile description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFullTimeEmployees() {
        return this.fullTimeEmployees;
    }

    public TickerProfile fullTimeEmployees(Integer fullTimeEmployees) {
        this.fullTimeEmployees = fullTimeEmployees;
        return this;
    }

    public void setFullTimeEmployees(Integer fullTimeEmployees) {
        this.fullTimeEmployees = fullTimeEmployees;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TickerProfile)) {
            return false;
        }
        return id != null && id.equals(((TickerProfile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TickerProfile{" +
            "id=" + getId() +
            ", tickerSymbol='" + getTickerSymbol() + "'" +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", industry='" + getIndustry() + "'" +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", website='" + getWebsite() + "'" +
            ", description='" + getDescription() + "'" +
            ", fullTimeEmployees=" + getFullTimeEmployees() +
            "}";
    }
}
