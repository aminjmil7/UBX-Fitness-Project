package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Equipement} entity.
 */
public class EquipementDTO implements Serializable {

    private Long id;

    private String modelName;

    private String modelNumber;

    private String instruction;

    private Boolean verified;

    private ParkDTO park;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public ParkDTO getPark() {
        return park;
    }

    public void setPark(ParkDTO park) {
        this.park = park;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipementDTO)) {
            return false;
        }

        EquipementDTO equipementDTO = (EquipementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, equipementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipementDTO{" +
            "id=" + getId() +
            ", modelName='" + getModelName() + "'" +
            ", modelNumber='" + getModelNumber() + "'" +
            ", instruction='" + getInstruction() + "'" +
            ", verified='" + getVerified() + "'" +
            ", park=" + getPark() +
            "}";
    }
}
