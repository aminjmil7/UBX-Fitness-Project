package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Report} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ReportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReportCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter mail;

    private StringFilter message;

    private InstantFilter date;

    private LongFilter mediaId;

    private LongFilter equipementId;

    private LongFilter parkId;

    public ReportCriteria() {}

    public ReportCriteria(ReportCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.mail = other.mail == null ? null : other.mail.copy();
        this.message = other.message == null ? null : other.message.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.mediaId = other.mediaId == null ? null : other.mediaId.copy();
        this.equipementId = other.equipementId == null ? null : other.equipementId.copy();
        this.parkId = other.parkId == null ? null : other.parkId.copy();
    }

    @Override
    public ReportCriteria copy() {
        return new ReportCriteria(this);
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

    public StringFilter getMail() {
        return mail;
    }

    public StringFilter mail() {
        if (mail == null) {
            mail = new StringFilter();
        }
        return mail;
    }

    public void setMail(StringFilter mail) {
        this.mail = mail;
    }

    public StringFilter getMessage() {
        return message;
    }

    public StringFilter message() {
        if (message == null) {
            message = new StringFilter();
        }
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public InstantFilter getDate() {
        return date;
    }

    public InstantFilter date() {
        if (date == null) {
            date = new InstantFilter();
        }
        return date;
    }

    public void setDate(InstantFilter date) {
        this.date = date;
    }

    public LongFilter getMediaId() {
        return mediaId;
    }

    public LongFilter mediaId() {
        if (mediaId == null) {
            mediaId = new LongFilter();
        }
        return mediaId;
    }

    public void setMediaId(LongFilter mediaId) {
        this.mediaId = mediaId;
    }

    public LongFilter getEquipementId() {
        return equipementId;
    }

    public LongFilter equipementId() {
        if (equipementId == null) {
            equipementId = new LongFilter();
        }
        return equipementId;
    }

    public void setEquipementId(LongFilter equipementId) {
        this.equipementId = equipementId;
    }

    public LongFilter getParkId() {
        return parkId;
    }

    public LongFilter parkId() {
        if (parkId == null) {
            parkId = new LongFilter();
        }
        return parkId;
    }

    public void setParkId(LongFilter parkId) {
        this.parkId = parkId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReportCriteria that = (ReportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(mail, that.mail) &&
            Objects.equals(message, that.message) &&
            Objects.equals(date, that.date) &&
            Objects.equals(mediaId, that.mediaId) &&
            Objects.equals(equipementId, that.equipementId) &&
            Objects.equals(parkId, that.parkId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mail, message, date, mediaId, equipementId, parkId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (mail != null ? "mail=" + mail + ", " : "") +
            (message != null ? "message=" + message + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (mediaId != null ? "mediaId=" + mediaId + ", " : "") +
            (equipementId != null ? "equipementId=" + equipementId + ", " : "") +
            (parkId != null ? "parkId=" + parkId + ", " : "") +
            "}";
    }
}
