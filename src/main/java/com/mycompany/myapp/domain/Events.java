package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Events.
 */
@Entity
@Table(name = "Events")
public class Events implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_date")
    private Instant eventDate;

    @Column(name = "user_id")
    private Integer user_id;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Events id(Long id) {
        this.id = id;
        return this;
    }

    public String getEventName() {
        return this.eventName;
    }

    public Events eventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Instant getEventDate() {
        return this.eventDate;
    }

    public Events eventDate(Instant eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    public void setEventDate(Instant eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getUser_id() {
        return this.user_id;
    }

    public Events user_id(Integer user_id) {
        this.user_id = user_id;
        return this;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Events)) {
            return false;
        }
        return id != null && id.equals(((Events) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Events{" +
            "id=" + getId() +
            ", eventName='" + getEventName() + "'" +
            ", eventDate='" + getEventDate() + "'" +
            ", user_id=" + getUser_id() +
            "}";
    }
}
