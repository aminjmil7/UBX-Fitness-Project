package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.AuthType;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Media.
 */
@Entity
@Table(name = "media")
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_type")
    private String fileType;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type")
    private AuthType authType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "equipements", "media", "reports" }, allowSetters = true)
    private Park park;

    @ManyToOne
    @JsonIgnoreProperties(value = { "reports", "media", "park" }, allowSetters = true)
    private Equipement equipement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "media", "equipement", "park" }, allowSetters = true)
    private Report report;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Media id(Long id) {
        this.id = id;
        return this;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Media fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public Media filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return this.fileType;
    }

    public Media fileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public AuthType getAuthType() {
        return this.authType;
    }

    public Media authType(AuthType authType) {
        this.authType = authType;
        return this;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public Park getPark() {
        return this.park;
    }

    public Media park(Park park) {
        this.setPark(park);
        return this;
    }

    public void setPark(Park park) {
        this.park = park;
    }

    public Equipement getEquipement() {
        return this.equipement;
    }

    public Media equipement(Equipement equipement) {
        this.setEquipement(equipement);
        return this;
    }

    public void setEquipement(Equipement equipement) {
        this.equipement = equipement;
    }

    public Report getReport() {
        return this.report;
    }

    public Media report(Report report) {
        this.setReport(report);
        return this;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Media)) {
            return false;
        }
        return id != null && id.equals(((Media) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Media{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", authType='" + getAuthType() + "'" +
            "}";
    }
}
