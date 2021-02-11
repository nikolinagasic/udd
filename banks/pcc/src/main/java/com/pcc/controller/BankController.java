package com.pcc.controller;

import com.pcc.model.dtos.PCCRequestDTO;
import com.pcc.model.dtos.PCCResponseDTO;
import com.pcc.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/bank")
public class BankController {

    @Autowired
    private BankService bankService;

    @PostMapping(value = "")
    public PCCResponseDTO findIssuerBank(@RequestBody PCCRequestDTO pccRequestDTO) {
        return this.bankService.findIssuerBank(pccRequestDTO);
    }
}
