package com.la.model.mappers;

import com.la.model.dtos.SubscriptionRequestDTO;
import com.la.model.SubscriptionRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class SubscriptionRequestDTOMapper implements MapperInterface<SubscriptionRequest, SubscriptionRequestDTO> {

    private ModelMapper modelMapper;

    @Autowired
    public SubscriptionRequestDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public SubscriptionRequest toEntity(SubscriptionRequestDTO dto) throws ParseException {
        return modelMapper.map(dto, SubscriptionRequest.class);
    }

    @Override
    public SubscriptionRequestDTO toDto(SubscriptionRequest entity) {
        return modelMapper.map(entity, SubscriptionRequestDTO.class);
    }
}
