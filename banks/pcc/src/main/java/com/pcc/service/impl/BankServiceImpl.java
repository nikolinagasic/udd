package com.pcc.service.impl;

import com.pcc.model.dtos.PCCRequestDTO;
import com.pcc.model.dtos.PCCResponseDTO;
import com.pcc.model.enums.Status;
import com.pcc.repository.BankRepository;
import com.pcc.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public PCCResponseDTO findIssuerBank(PCCRequestDTO pccRequestDTO) {
        String pan = pccRequestDTO.getPan().substring(0,3);
        try {
            switch (this.bankRepository.findBankByPan(pan).getName()) {
                case "bank-a":
                    return restTemplate.exchange("https://bank-a/transaction/second",
                            HttpMethod.POST, new HttpEntity<>(pccRequestDTO), new ParameterizedTypeReference<PCCResponseDTO>() {}).getBody();
                case "bank-b":
                    return restTemplate.exchange("https://bank-b/transaction/second",
                            HttpMethod.POST, new HttpEntity<>(pccRequestDTO), new ParameterizedTypeReference<PCCResponseDTO>() {}).getBody();
                default:
                    return new PCCResponseDTO(pccRequestDTO.getAcquirerOrderId(), pccRequestDTO.getAcquirerTimestamp(), null, null, Status.ERROR);
            }
        } catch (NullPointerException e) {
            return new PCCResponseDTO(pccRequestDTO.getAcquirerOrderId(), pccRequestDTO.getAcquirerTimestamp(), null, null, Status.ERROR);
        }
    }
}
