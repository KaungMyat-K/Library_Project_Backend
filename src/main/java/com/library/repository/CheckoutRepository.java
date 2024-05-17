package com.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.entity.CheckoutEntity;

@Repository
public interface CheckoutRepository extends JpaRepository<CheckoutEntity,Long> {
    
    CheckoutEntity findByUserEmailAndBookId(String userEmail,Long bookId);

    List<CheckoutEntity> findBooksByUserEmail(String userEmail);
}
    