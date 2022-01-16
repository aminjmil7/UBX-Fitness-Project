package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.ParkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Park} and its DTO {@link ParkDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ParkMapper extends EntityMapper<ParkDTO, Park> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParkDTO toDtoId(Park park);
}
