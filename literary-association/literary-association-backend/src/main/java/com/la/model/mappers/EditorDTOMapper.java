package com.la.model.mappers;

import com.la.model.dtos.EditorDTO;
import com.la.model.users.Editor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class EditorDTOMapper implements MapperInterface<Editor, EditorDTO>{

    private ModelMapper modelMapper;

    @Autowired
    public EditorDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Editor toEntity(EditorDTO dto) throws ParseException {
        return modelMapper.map(dto, Editor.class);
    }

    @Override
    public EditorDTO toDTO(Editor entity) {
        return modelMapper.map(entity, EditorDTO.class);
    }
}
