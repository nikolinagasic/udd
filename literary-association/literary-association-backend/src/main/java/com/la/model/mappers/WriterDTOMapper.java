package com.la.model.mappers;

import com.la.model.dtos.WriterDTO;
import com.la.model.users.Writer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class WriterDTOMapper implements MapperInterface<Writer, WriterDTO>{

    private ModelMapper modelMapper;

    @Autowired
    public WriterDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Writer toEntity(WriterDTO dto) throws ParseException {
        return modelMapper.map(dto, Writer.class);
    }

    @Override
    public WriterDTO toDTO(Writer entity) {
        return modelMapper.map(entity, WriterDTO.class);
    }
}
