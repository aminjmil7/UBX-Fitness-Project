package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Equipement} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.EquipementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /equipements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EquipementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter modelName;

    private StringFilter modelNumber;

    private StringFilter instruction;

    private BooleanFilter verified;

    private LongFilter reportId;

    private LongFilter mediaId;

    private LongFilter parkId;

    public EquipementCriteria() {}

    public EquipementCriteria(EquipementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.modelName = other.modelName == null ? null : other.modelName.copy();
        this.modelNumber = other.modelNumber == null ? null : other.modelNumber.copy();
        this.instruction = other.instruction == null ? null : other.instruction.copy();
        this.verified = other.verified == null ? null : other.verified.copy();
        this.reportId = other.reportId == null ? null : other.reportId.copy();
        this.mediaId = other.mediaId == null ? null : other.mediaId.copy();
        this.parkId = other.parkId == null ? null : other.parkId.copy();
    }

    @Override
    public EquipementCriteria copy() {
        return new EquipementCriteria(this);
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

    public StringFilter getModelName() {
        return modelName;
    }

    public StringFilter modelName() {
        if (modelName == null) {
            modelName = new StringFilter();
        }
        return modelName;
    }

    public void setModelName(StringFilter modelName) {
        this.modelName = modelName;
    }

    public StringFilter getModelNumber() {
        return modelNumber;
    }

    public StringFilter modelNumber() {
        if (modelNumber == null) {
            modelNumber = new StringFilter();
        }
        return modelNumber;
    }

    public void setModelNumber(StringFilter modelNumber) {
        this.modelNumber = modelNumber;
    }

    public StringFilter getInstruction() {
        return instruction;
    }

    public StringFilter instruction() {
        if (instruction == null) {
            instruction = new StringFilter();
        }
        return instruction;
    }

    public void setInstruction(StringFilter instruction) {
        this.instruction = instruction;
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
        final EquipementCriteria that = (EquipementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(modelName, that.modelName) &&
            Objects.equals(modelNumber, that.modelNumber) &&
            Objects.equals(instruction, that.instruction) &&
            Objects.equals(verified, that.verified) &&
            Objects.equals(reportId, that.reportId) &&
            Objects.equals(mediaId, that.mediaId) &&
            Objects.equals(parkId, that.parkId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, modelName, modelNumber, instruction, verified, reportId, mediaId, parkId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipementCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (modelName != null ? "modelName=" + modelName + ", " : "") +
            (modelNumber != null ? "modelNumber=" + modelNumber + ", " : "") +
            (instruction != null ? "instruction=" + instruction + ", " : "") +
            (verified != null ? "verified=" + verified + ", " : "") +
            (reportId != null ? "reportId=" + reportId + ", " : "") +
            (mediaId != null ? "mediaId=" + mediaId + ", " : "") +
            (parkId != null ? "parkId=" + parkId + ", " : "") +
            "}";
    }
}
