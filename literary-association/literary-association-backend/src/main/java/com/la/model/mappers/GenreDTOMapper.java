package com.la.model.mappers;

import com.la.model.Genre;
import com.la.model.dtos.GenreDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class GenreDTOMapper implements MapperInterface<Genre, GenreDTO> {

    private ModelMapper modelMapper;

    @Autowired
    public GenreDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Genre toEntity(GenreDTO dto) throws ParseException {
        return modelMapper.map(dto, Genre.class);
    }

    @Override
    public GenreDTO toDTO(Genre entity) {
        return modelMapper.map(entity, GenreDTO.class);
    }
}
