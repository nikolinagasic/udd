package com.la.model.mappers;

import java.text.ParseException;

public interface MapperInterface<T, U> {
    T toEntity(U dto) throws ParseException;

    U toDto(T entity);
}
