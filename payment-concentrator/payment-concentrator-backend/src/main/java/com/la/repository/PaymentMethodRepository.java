package com.la.repository;

import com.la.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    @Query(value = "SELECT * FROM payment_method p WHERE p.id NOT IN (1, 2, 3)", nativeQuery = true)
    List<PaymentMethod> getAllWithoutFirstThree();

    PaymentMethod findByName(String name);
}
