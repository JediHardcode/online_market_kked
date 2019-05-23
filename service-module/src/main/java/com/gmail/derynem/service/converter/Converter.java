package com.gmail.derynem.service.converter;

public interface Converter<ObjectDTO, Object> {
    ObjectDTO toDTO(Object object);

    Object toEntity(ObjectDTO ObjectDTO);
}