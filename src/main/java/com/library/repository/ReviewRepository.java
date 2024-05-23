package com.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;


import com.library.entity.ReviewEntity;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity,Long> {
    
    Page<ReviewEntity> findByBookId(@RequestParam("book_id") String bookId,Pageable pageable);

    ReviewEntity findByUserEmailAndBookId(String userEmail,Long bookId);

}
