package com.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.library.entity.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity,Long> {
    
    Page<BookEntity> findByTitleContaining(@RequestParam("title") String title,Pageable pageable);

    Page<BookEntity> findByCategory(@RequestParam("category") String category,Pageable pageable);



}
