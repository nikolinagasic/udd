package com.la.model.mappers;

import com.la.model.dtos.LectorDTO;
import com.la.model.users.Lector;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class LectorDTOMapper implements MapperInterface<Lector, LectorDTO> {

    private ModelMapper modelMapper;

    @Autowired
    public LectorDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Lector toEntity(LectorDTO dto) throws ParseException {
        return modelMapper.map(dto, Lector.class);
    }

    @Override
    public LectorDTO toDTO(Lector entity) {
        return modelMapper.map(entity, LectorDTO.class);
    }
}
