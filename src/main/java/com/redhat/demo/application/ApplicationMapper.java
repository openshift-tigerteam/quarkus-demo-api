package com.redhat.demo.application;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface ApplicationMapper {
    
    Application toDomain(ApplicationEntity entity);
    
    ApplicationEntity toEntity(Application domain);
    
}