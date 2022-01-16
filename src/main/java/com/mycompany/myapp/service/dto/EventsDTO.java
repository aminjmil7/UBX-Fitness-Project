package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Events} entity.
 */
public class EventsDTO implements Serializable {

    private Long id;

    private String eventName;

    private Instant eventDate;

    private Integer user_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Instant getEventDate() {
        return eventDate;
    }

    public void setEventDate(Instant eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventsDTO)) {
            return false;
        }

        EventsDTO eventsDTO = (EventsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventsDTO{" +
            "id=" + getId() +
            ", eventName='" + getEventName() + "'" +
            ", eventDate='" + getEventDate() + "'" +
            ", user_id=" + getUser_id() +
            "}";
    }
}
