package com.la.service.impl;

import com.la.model.dtos.BuyerRequestDTO;
import com.la.model.dtos.PaymentMethodDTO;
import com.la.model.mappers.PaymentMethodDTOMapper;
import com.la.model.BuyerRequest;
import com.la.model.PaymentMethod;
import com.la.repository.PaymentMethodRepository;
import com.la.repository.BuyerRequestRepository;
import com.la.repository.SubscriberRepository;
import com.la.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private BuyerRequestRepository buyerRequestRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PaymentMethodDTOMapper mapper;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Override
    public List<PaymentMethod> getAll() {
        return paymentMethodRepository.findAll();
    }

    @Override
    public List<PaymentMethod> getAllWithoutFirstThree() {
        return paymentMethodRepository.getAllWithoutFirstThree();
    }

    @Override
    public Long createPaymentMethod(PaymentMethodDTO paymentMethodDTO) throws ParseException, NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        PaymentMethod p = paymentMethodRepository.save(mapper.toEntity(paymentMethodDTO));
        paymentMethodRepository.flush();
        createService(p.getName());
        return p.getId();
    }

    @Override
    public boolean deletePaymentMethod(Long paymentMethodId) {
        if (paymentMethodId.equals(1L) || paymentMethodId.equals(2L) || paymentMethodId.equals(3L)) {
            return false;
        }
        try {
            PaymentMethod method = paymentMethodRepository.getOne(paymentMethodId);
            deleteService(method.getName());
            paymentMethodRepository.deleteById(paymentMethodId);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private void deleteService(String name) {
        File sourceFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\la\\service\\" + name + "Service.java");
        if (sourceFile.delete()) {
            System.out.println("Deleted successfully");
        } else {
            System.out.println("Not deleted");
        }
    }

    private void createService(String name) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        File sourceFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\la\\service\\" + name + "Service.java");
        if (sourceFile.createNewFile()) {
            System.out.println("hej");
        }
        String sourceCode =
                "package com.la.service;\n\n" + "public interface " + name + "Service " + "{ }";

        // write the source code into the source file
        FileWriter writer = new FileWriter(sourceFile);
        writer.write(sourceCode);
        writer.flush();
        writer.close();
    }

    @Override
    public String getPaymentMethodsUrl(BuyerRequestDTO buyerRequestDTO, String username) {
        if (subscriberRepository.findByUsername(username) != null){
            Set<PaymentMethod> paymentMethods = (subscriberRepository.findByUsername(username)).getPaymentMethods();
            if(paymentMethods.size() != 0) {
                BuyerRequest buyerRequest = new BuyerRequest();
                buyerRequest.setMerchantOrderId(buyerRequestDTO.getMerchantOrderId());
                buyerRequest.setAmount(buyerRequestDTO.getAmount());
                buyerRequest.setMerchantTimestamp(buyerRequestDTO.getMerchantTimestamp());
                buyerRequest.setSubscriber(subscriberRepository.findByUsername(username));
                buyerRequest.setTimestamp(LocalDateTime.now());
                buyerRequest = buyerRequestRepository.save(buyerRequest);

                return "https://localhost:3001/payment-methods/" + buyerRequest.getId();
            }
            return subscriberRepository.findByUsername(username).getSubscriberDetails().getErrorUrl();
        }

        return null;
    }
}
