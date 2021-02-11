package com.la.service;

import com.la.model.dtos.TransactionDTO;

import java.util.List;

public interface TransactionService {

    List<TransactionDTO> getAll();

    List<TransactionDTO> findByUser(String username);
}
