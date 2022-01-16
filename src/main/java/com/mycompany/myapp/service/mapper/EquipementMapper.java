package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.EquipementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Equipement} and its DTO {@link EquipementDTO}.
 */
@Mapper(componentModel = "spring", uses = { ParkMapper.class })
public interface EquipementMapper extends EntityMapper<EquipementDTO, Equipement> {
    @Mapping(target = "park", source = "park", qualifiedByName = "id")
    EquipementDTO toDto(Equipement s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EquipementDTO toDtoId(Equipement equipement);
}
