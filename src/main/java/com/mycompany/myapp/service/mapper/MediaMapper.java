package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.MediaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Media} and its DTO {@link MediaDTO}.
 */
@Mapper(componentModel = "spring", uses = { ParkMapper.class, EquipementMapper.class, ReportMapper.class })
public interface MediaMapper extends EntityMapper<MediaDTO, Media> {
    @Mapping(target = "park", source = "park", qualifiedByName = "id")
    @Mapping(target = "equipement", source = "equipement", qualifiedByName = "id")
    @Mapping(target = "report", source = "report", qualifiedByName = "id")
    MediaDTO toDto(Media s);
}
