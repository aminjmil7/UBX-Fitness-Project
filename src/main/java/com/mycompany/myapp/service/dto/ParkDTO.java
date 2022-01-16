package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Park} entity.
 */
public class ParkDTO implements Serializable {

    private Long id;

    private String parkName;

    private String parkAddress;

    private BigDecimal longtitude;

    private BigDecimal latitude;

    private Boolean verified;

    private Instant dateInstall;

    private Instant dateOpen;

    private Instant dateClose;

    private String note;

    private String reseller;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getParkAddress() {
        return parkAddress;
    }

    public void setParkAddress(String parkAddress) {
        this.parkAddress = parkAddress;
    }

    public BigDecimal getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(BigDecimal longtitude) {
        this.longtitude = longtitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Instant getDateInstall() {
        return dateInstall;
    }

    public void setDateInstall(Instant dateInstall) {
        this.dateInstall = dateInstall;
    }

    public Instant getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(Instant dateOpen) {
        this.dateOpen = dateOpen;
    }

    public Instant getDateClose() {
        return dateClose;
    }

    public void setDateClose(Instant dateClose) {
        this.dateClose = dateClose;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReseller() {
        return reseller;
    }

    public void setReseller(String reseller) {
        this.reseller = reseller;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParkDTO)) {
            return false;
        }

        ParkDTO parkDTO = (ParkDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, parkDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParkDTO{" +
            "id=" + getId() +
            ", parkName='" + getParkName() + "'" +
            ", parkAddress='" + getParkAddress() + "'" +
            ", longtitude=" + getLongtitude() +
            ", latitude=" + getLatitude() +
            ", verified='" + getVerified() + "'" +
            ", dateInstall='" + getDateInstall() + "'" +
            ", dateOpen='" + getDateOpen() + "'" +
            ", dateClose='" + getDateClose() + "'" +
            ", note='" + getNote() + "'" +
            ", reseller='" + getReseller() + "'" +
            "}";
    }
}
