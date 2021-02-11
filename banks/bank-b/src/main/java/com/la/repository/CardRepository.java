package com.la.repository;

import com.la.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByPanAndSecurityCodeAndExpireDateAndCardholderName(String pan, String securityCode, String expireDate, String cardholderName);
}
