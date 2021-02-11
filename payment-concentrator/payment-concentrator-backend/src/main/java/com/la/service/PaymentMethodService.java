package com.la.service;

import com.la.model.dtos.BuyerRequestDTO;
import com.la.model.dtos.PaymentMethodDTO;
import com.la.model.PaymentMethod;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

public interface PaymentMethodService {

        List<PaymentMethod> getAll();

        List<PaymentMethod> getAllWithoutFirstThree();

        Long createPaymentMethod(PaymentMethodDTO paymentMethodDTO) throws ParseException, NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException;

        boolean deletePaymentMethod(Long paymentMethodId);

        String getPaymentMethodsUrl(BuyerRequestDTO buyerRequestDTO, String username);

}
