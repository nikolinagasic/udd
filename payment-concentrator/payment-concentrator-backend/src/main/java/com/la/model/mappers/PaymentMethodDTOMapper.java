package com.la.model.mappers;

import com.la.model.dtos.PaymentMethodDTO;
import com.la.model.PaymentMethod;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class PaymentMethodDTOMapper implements MapperInterface<PaymentMethod, PaymentMethodDTO> {

    private ModelMapper modelMapper;

    @Autowired
    public PaymentMethodDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PaymentMethod toEntity(PaymentMethodDTO dto) throws ParseException {
        return modelMapper.map(dto, PaymentMethod.class);
    }

    @Override
    public PaymentMethodDTO toDto(PaymentMethod entity) {
        return modelMapper.map(entity, PaymentMethodDTO.class);
    }
}
