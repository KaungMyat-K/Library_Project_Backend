package com.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.library.entity.HistoryEntity;


@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity,Long>{
    Page<HistoryEntity> findBooksByUserEmail(@RequestParam("email") String userEmail, Pageable pageable);
}
