package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.AuthType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Media} entity.
 */
public class MediaDTO implements Serializable {

    private Long id;

    private String fileName;

    private String filePath;

    private String fileType;

    private AuthType authType;

    private ParkDTO park;

    private EquipementDTO equipement;

    private ReportDTO report;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public ParkDTO getPark() {
        return park;
    }

    public void setPark(ParkDTO park) {
        this.park = park;
    }

    public EquipementDTO getEquipement() {
        return equipement;
    }

    public void setEquipement(EquipementDTO equipement) {
        this.equipement = equipement;
    }

    public ReportDTO getReport() {
        return report;
    }

    public void setReport(ReportDTO report) {
        this.report = report;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MediaDTO)) {
            return false;
        }

        MediaDTO mediaDTO = (MediaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mediaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MediaDTO{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", authType='" + getAuthType() + "'" +
            ", park=" + getPark() +
            ", equipement=" + getEquipement() +
            ", report=" + getReport() +
            "}";
    }
}
