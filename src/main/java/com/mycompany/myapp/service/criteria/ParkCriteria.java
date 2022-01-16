package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Park} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ParkResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /parks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ParkCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter parkName;

    private StringFilter parkAddress;

    private BigDecimalFilter longtitude;

    private BigDecimalFilter latitude;

    private BooleanFilter verified;

    private InstantFilter dateInstall;

    private InstantFilter dateOpen;

    private InstantFilter dateClose;

    private StringFilter note;

    private StringFilter reseller;

    private LongFilter equipementId;

    private LongFilter mediaId;

    private LongFilter reportId;

    public ParkCriteria() {}

    public ParkCriteria(ParkCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.parkName = other.parkName == null ? null : other.parkName.copy();
        this.parkAddress = other.parkAddress == null ? null : other.parkAddress.copy();
        this.longtitude = other.longtitude == null ? null : other.longtitude.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.verified = other.verified == null ? null : other.verified.copy();
        this.dateInstall = other.dateInstall == null ? null : other.dateInstall.copy();
        this.dateOpen = other.dateOpen == null ? null : other.dateOpen.copy();
        this.dateClose = other.dateClose == null ? null : other.dateClose.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.reseller = other.reseller == null ? null : other.reseller.copy();
        this.equipementId = other.equipementId == null ? null : other.equipementId.copy();
        this.mediaId = other.mediaId == null ? null : other.mediaId.copy();
        this.reportId = other.reportId == null ? null : other.reportId.copy();
    }

    @Override
    public ParkCriteria copy() {
        return new ParkCriteria(this);
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

    public StringFilter getParkName() {
        return parkName;
    }

    public StringFilter parkName() {
        if (parkName == null) {
            parkName = new StringFilter();
        }
        return parkName;
    }

    public void setParkName(StringFilter parkName) {
        this.parkName = parkName;
    }

    public StringFilter getParkAddress() {
        return parkAddress;
    }

    public StringFilter parkAddress() {
        if (parkAddress == null) {
            parkAddress = new StringFilter();
        }
        return parkAddress;
    }

    public void setParkAddress(StringFilter parkAddress) {
        this.parkAddress = parkAddress;
    }

    public BigDecimalFilter getLongtitude() {
        return longtitude;
    }

    public BigDecimalFilter longtitude() {
        if (longtitude == null) {
            longtitude = new BigDecimalFilter();
        }
        return longtitude;
    }

    public void setLongtitude(BigDecimalFilter longtitude) {
        this.longtitude = longtitude;
    }

    public BigDecimalFilter getLatitude() {
        return latitude;
    }

    public BigDecimalFilter latitude() {
        if (latitude == null) {
            latitude = new BigDecimalFilter();
        }
        return latitude;
    }

    public void setLatitude(BigDecimalFilter latitude) {
        this.latitude = latitude;
    }

    public BooleanFilter getVerified() {
        return verified;
    }

    public BooleanFilter verified() {
        if (verified == null) {
            verified = new BooleanFilter();
        }
        return verified;
    }

    public void setVerified(BooleanFilter verified) {
        this.verified = verified;
    }

    public InstantFilter getDateInstall() {
        return dateInstall;
    }

    public InstantFilter dateInstall() {
        if (dateInstall == null) {
            dateInstall = new InstantFilter();
        }
        return dateInstall;
    }

    public void setDateInstall(InstantFilter dateInstall) {
        this.dateInstall = dateInstall;
    }

    public InstantFilter getDateOpen() {
        return dateOpen;
    }

    public InstantFilter dateOpen() {
        if (dateOpen == null) {
            dateOpen = new InstantFilter();
        }
        return dateOpen;
    }

    public void setDateOpen(InstantFilter dateOpen) {
        this.dateOpen = dateOpen;
    }

    public InstantFilter getDateClose() {
        return dateClose;
    }

    public InstantFilter dateClose() {
        if (dateClose == null) {
            dateClose = new InstantFilter();
        }
        return dateClose;
    }

    public void setDateClose(InstantFilter dateClose) {
        this.dateClose = dateClose;
    }

    public StringFilter getNote() {
        return note;
    }

    public StringFilter note() {
        if (note == null) {
            note = new StringFilter();
        }
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public StringFilter getReseller() {
        return reseller;
    }

    public StringFilter reseller() {
        if (reseller == null) {
            reseller = new StringFilter();
        }
        return reseller;
    }

    public void setReseller(StringFilter reseller) {
        this.reseller = reseller;
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

    public LongFilter getReportId() {
        return reportId;
    }

    public LongFilter reportId() {
        if (reportId == null) {
            reportId = new LongFilter();
        }
        return reportId;
    }

    public void setReportId(LongFilter reportId) {
        this.reportId = reportId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ParkCriteria that = (ParkCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(parkName, that.parkName) &&
            Objects.equals(parkAddress, that.parkAddress) &&
            Objects.equals(longtitude, that.longtitude) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(verified, that.verified) &&
            Objects.equals(dateInstall, that.dateInstall) &&
            Objects.equals(dateOpen, that.dateOpen) &&
            Objects.equals(dateClose, that.dateClose) &&
            Objects.equals(note, that.note) &&
            Objects.equals(reseller, that.reseller) &&
            Objects.equals(equipementId, that.equipementId) &&
            Objects.equals(mediaId, that.mediaId) &&
            Objects.equals(reportId, that.reportId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            parkName,
            parkAddress,
            longtitude,
            latitude,
            verified,
            dateInstall,
            dateOpen,
            dateClose,
            note,
            reseller,
            equipementId,
            mediaId,
            reportId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParkCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (parkName != null ? "parkName=" + parkName + ", " : "") +
            (parkAddress != null ? "parkAddress=" + parkAddress + ", " : "") +
            (longtitude != null ? "longtitude=" + longtitude + ", " : "") +
            (latitude != null ? "latitude=" + latitude + ", " : "") +
            (verified != null ? "verified=" + verified + ", " : "") +
            (dateInstall != null ? "dateInstall=" + dateInstall + ", " : "") +
            (dateOpen != null ? "dateOpen=" + dateOpen + ", " : "") +
            (dateClose != null ? "dateClose=" + dateClose + ", " : "") +
            (note != null ? "note=" + note + ", " : "") +
            (reseller != null ? "reseller=" + reseller + ", " : "") +
            (equipementId != null ? "equipementId=" + equipementId + ", " : "") +
            (mediaId != null ? "mediaId=" + mediaId + ", " : "") +
            (reportId != null ? "reportId=" + reportId + ", " : "") +
            "}";
    }
}
