package com.pcc.service;

import com.pcc.model.dtos.PCCRequestDTO;
import com.pcc.model.dtos.PCCResponseDTO;

public interface BankService {
    PCCResponseDTO findIssuerBank(PCCRequestDTO pccRequestDTO);
}
