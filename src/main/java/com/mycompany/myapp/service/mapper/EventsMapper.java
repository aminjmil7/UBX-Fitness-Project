package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.EventsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Events} and its DTO {@link EventsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EventsMapper extends EntityMapper<EventsDTO, Events> {}
