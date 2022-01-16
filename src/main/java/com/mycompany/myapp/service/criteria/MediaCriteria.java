package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.AuthType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Media} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.MediaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /media?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MediaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AuthType
     */
    public static class AuthTypeFilter extends Filter<AuthType> {

        public AuthTypeFilter() {}

        public AuthTypeFilter(AuthTypeFilter filter) {
            super(filter);
        }

        @Override
        public AuthTypeFilter copy() {
            return new AuthTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fileName;

    private StringFilter filePath;

    private StringFilter fileType;

    private AuthTypeFilter authType;

    private LongFilter parkId;

    private LongFilter equipementId;

    private LongFilter reportId;

    public MediaCriteria() {}

    public MediaCriteria(MediaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fileName = other.fileName == null ? null : other.fileName.copy();
        this.filePath = other.filePath == null ? null : other.filePath.copy();
        this.fileType = other.fileType == null ? null : other.fileType.copy();
        this.authType = other.authType == null ? null : other.authType.copy();
        this.parkId = other.parkId == null ? null : other.parkId.copy();
        this.equipementId = other.equipementId == null ? null : other.equipementId.copy();
        this.reportId = other.reportId == null ? null : other.reportId.copy();
    }

    @Override
    public MediaCriteria copy() {
        return new MediaCriteria(this);
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

    public StringFilter getFileName() {
        return fileName;
    }

    public StringFilter fileName() {
        if (fileName == null) {
            fileName = new StringFilter();
        }
        return fileName;
    }

    public void setFileName(StringFilter fileName) {
        this.fileName = fileName;
    }

    public StringFilter getFilePath() {
        return filePath;
    }

    public StringFilter filePath() {
        if (filePath == null) {
            filePath = new StringFilter();
        }
        return filePath;
    }

    public void setFilePath(StringFilter filePath) {
        this.filePath = filePath;
    }

    public StringFilter getFileType() {
        return fileType;
    }

    public StringFilter fileType() {
        if (fileType == null) {
            fileType = new StringFilter();
        }
        return fileType;
    }

    public void setFileType(StringFilter fileType) {
        this.fileType = fileType;
    }

    public AuthTypeFilter getAuthType() {
        return authType;
    }

    public AuthTypeFilter authType() {
        if (authType == null) {
            authType = new AuthTypeFilter();
        }
        return authType;
    }

    public void setAuthType(AuthTypeFilter authType) {
        this.authType = authType;
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
        final MediaCriteria that = (MediaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(filePath, that.filePath) &&
            Objects.equals(fileType, that.fileType) &&
            Objects.equals(authType, that.authType) &&
            Objects.equals(parkId, that.parkId) &&
            Objects.equals(equipementId, that.equipementId) &&
            Objects.equals(reportId, that.reportId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, filePath, fileType, authType, parkId, equipementId, reportId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MediaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fileName != null ? "fileName=" + fileName + ", " : "") +
            (filePath != null ? "filePath=" + filePath + ", " : "") +
            (fileType != null ? "fileType=" + fileType + ", " : "") +
            (authType != null ? "authType=" + authType + ", " : "") +
            (parkId != null ? "parkId=" + parkId + ", " : "") +
            (equipementId != null ? "equipementId=" + equipementId + ", " : "") +
            (reportId != null ? "reportId=" + reportId + ", " : "") +
            "}";
    }
}
