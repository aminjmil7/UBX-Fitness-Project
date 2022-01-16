package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Events} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.EventsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter eventName;

    private InstantFilter eventDate;

    private IntegerFilter user_id;

    public EventsCriteria() {}

    public EventsCriteria(EventsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.eventName = other.eventName == null ? null : other.eventName.copy();
        this.eventDate = other.eventDate == null ? null : other.eventDate.copy();
        this.user_id = other.user_id == null ? null : other.user_id.copy();
    }

    @Override
    public EventsCriteria copy() {
        return new EventsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEventName() {
        return eventName;
    }

    public StringFilter eventName() {
        if (eventName == null) {
            eventName = new StringFilter();
        }
        return eventName;
    }

    public void setEventName(StringFilter eventName) {
        this.eventName = eventName;
    }

    public InstantFilter getEventDate() {
        return eventDate;
    }

    public InstantFilter eventDate() {
        if (eventDate == null) {
            eventDate = new InstantFilter();
        }
        return eventDate;
    }

    public void setEventDate(InstantFilter eventDate) {
        this.eventDate = eventDate;
    }

    public IntegerFilter getUser_id() {
        return user_id;
    }

    public IntegerFilter user_id() {
        if (user_id == null) {
            user_id = new IntegerFilter();
        }
        return user_id;
    }

    public void setUser_id(IntegerFilter user_id) {
        this.user_id = user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventsCriteria that = (EventsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(eventName, that.eventName) &&
            Objects.equals(eventDate, that.eventDate) &&
            Objects.equals(user_id, that.user_id)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventName, eventDate, user_id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (eventName != null ? "eventName=" + eventName + ", " : "") +
            (eventDate != null ? "eventDate=" + eventDate + ", " : "") +
            (user_id != null ? "user_id=" + user_id + ", " : "") +
            "}";
    }
}
