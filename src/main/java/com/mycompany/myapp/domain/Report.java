package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Report.
 */
@Entity
@Table(name = "report")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mail")
    private String mail;

    @Column(name = "message")
    private String message;

    @Column(name = "date")
    private Instant date;

    @OneToMany(mappedBy = "report")
    @JsonIgnoreProperties(value = { "park", "equipement", "report" }, allowSetters = true)
    private Set<Media> media = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "reports", "media", "park" }, allowSetters = true)
    private Equipement equipement;

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

    public Report id(Long id) {
        this.id = id;
        return this;
    }

    public String getMail() {
        return this.mail;
    }

    public Report mail(String mail) {
        this.mail = mail;
        return this;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMessage() {
        return this.message;
    }

    public Report message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getDate() {
        return this.date;
    }

    public Report date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Set<Media> getMedia() {
        return this.media;
    }

    public Report media(Set<Media> media) {
        this.setMedia(media);
        return this;
    }

    public Report addMedia(Media media) {
        this.media.add(media);
        media.setReport(this);
        return this;
    }

    public Report removeMedia(Media media) {
        this.media.remove(media);
        media.setReport(null);
        return this;
    }

    public void setMedia(Set<Media> media) {
        if (this.media != null) {
            this.media.forEach(i -> i.setReport(null));
        }
        if (media != null) {
            media.forEach(i -> i.setReport(this));
        }
        this.media = media;
    }

    public Equipement getEquipement() {
        return this.equipement;
    }

    public Report equipement(Equipement equipement) {
        this.setEquipement(equipement);
        return this;
    }

    public void setEquipement(Equipement equipement) {
        this.equipement = equipement;
    }

    public Park getPark() {
        return this.park;
    }

    public Report park(Park park) {
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
        if (!(o instanceof Report)) {
            return false;
        }
        return id != null && id.equals(((Report) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Report{" +
            "id=" + getId() +
            ", mail='" + getMail() + "'" +
            ", message='" + getMessage() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
