package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.ReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Report} and its DTO {@link ReportDTO}.
 */
@Mapper(componentModel = "spring", uses = { EquipementMapper.class, ParkMapper.class })
public interface ReportMapper extends EntityMapper<ReportDTO, Report> {
    @Mapping(target = "equipement", source = "equipement", qualifiedByName = "id")
    @Mapping(target = "park", source = "park", qualifiedByName = "id")
    ReportDTO toDto(Report s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportDTO toDtoId(Report report);
}
