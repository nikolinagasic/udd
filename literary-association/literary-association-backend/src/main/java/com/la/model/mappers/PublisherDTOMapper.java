package com.la.model.mappers;

import com.la.model.Publisher;
import com.la.model.dtos.PublisherDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class PublisherDTOMapper implements MapperInterface<Publisher, PublisherDTO> {

    private ModelMapper modelMapper;

    @Autowired
    public PublisherDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Publisher toEntity(PublisherDTO dto) throws ParseException {
        return modelMapper.map(dto, Publisher.class);
    }

    @Override
    public PublisherDTO toDTO(Publisher entity) {
        return modelMapper.map(entity, PublisherDTO.class);
    }
}
