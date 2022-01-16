package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Equipement.
 */
@Entity
@Table(name = "equipement")
public class Equipement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "model_number")
    private String modelNumber;

    @Column(name = "instruction")
    private String instruction;

    @Column(name = "verified")
    private Boolean verified;

    @OneToMany(mappedBy = "equipement")
    @JsonIgnoreProperties(value = { "media", "equipement", "park" }, allowSetters = true)
    private Set<Report> reports = new HashSet<>();

    @OneToMany(mappedBy = "equipement")
    @JsonIgnoreProperties(value = { "park", "equipement", "report" }, allowSetters = true)
    private Set<Media> media = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "equipements", "media", "reports" }, allowSetters = true)
    private Park park;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Equipement id(Long id) {
        this.id = id;
        return this;
    }

    public String getModelName() {
        return this.modelName;
    }

    public Equipement modelName(String modelName) {
        this.modelName = modelName;
        return this;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelNumber() {
        return this.modelNumber;
    }

    public Equipement modelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
        return this;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getInstruction() {
        return this.instruction;
    }

    public Equipement instruction(String instruction) {
        this.instruction = instruction;
        return this;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Boolean getVerified() {
        return this.verified;
    }

    public Equipement verified(Boolean verified) {
        this.verified = verified;
        return this;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Set<Report> getReports() {
        return this.reports;
    }

    public Equipement reports(Set<Report> reports) {
        this.setReports(reports);
        return this;
    }

    public Equipement addReport(Report report) {
        this.reports.add(report);
        report.setEquipement(this);
        return this;
    }

    public Equipement removeReport(Report report) {
        this.reports.remove(report);
        report.setEquipement(null);
        return this;
    }

    public void setReports(Set<Report> reports) {
        if (this.reports != null) {
            this.reports.forEach(i -> i.setEquipement(null));
        }
        if (reports != null) {
            reports.forEach(i -> i.setEquipement(this));
        }
        this.reports = reports;
    }

    public Set<Media> getMedia() {
        return this.media;
    }

    public Equipement media(Set<Media> media) {
        this.setMedia(media);
        return this;
    }

    public Equipement addMedia(Media media) {
        this.media.add(media);
        media.setEquipement(this);
        return this;
    }

    public Equipement removeMedia(Media media) {
        this.media.remove(media);
        media.setEquipement(null);
        return this;
    }

    public void setMedia(Set<Media> media) {
        if (this.media != null) {
            this.media.forEach(i -> i.setEquipement(null));
        }
        if (media != null) {
            media.forEach(i -> i.setEquipement(this));
        }
        this.media = media;
    }

    public Park getPark() {
        return this.park;
    }

    public Equipement park(Park park) {
        this.setPark(park);
        return this;
    }

    public void setPark(Park park) {
        this.park = park;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Equipement)) {
            return false;
        }
        return id != null && id.equals(((Equipement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Equipement{" +
            "id=" + getId() +
            ", modelName='" + getModelName() + "'" +
            ", modelNumber='" + getModelNumber() + "'" +
            ", instruction='" + getInstruction() + "'" +
            ", verified='" + getVerified() + "'" +
            "}";
    }
}
