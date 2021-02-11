package com.la.model.mappers;

import com.la.model.Script;
import com.la.model.dtos.ScriptDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class ScriptDTOMapper implements MapperInterface<Script, ScriptDTO> {

    private ModelMapper modelMapper;

    @Autowired
    public ScriptDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Script toEntity(ScriptDTO dto) throws ParseException {
        return modelMapper.map(dto, Script.class);
    }

    @Override
    public ScriptDTO toDTO(Script entity) {
        return modelMapper.map(entity, ScriptDTO.class);
    }
}
