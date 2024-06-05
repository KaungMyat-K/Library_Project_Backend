package com.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.library.entity.MessageEntity;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long>{

    Page<MessageEntity> findByUserEmail(@RequestParam("user_email") String userEmail, Pageable pageable);

    Page<MessageEntity> findByClosed(@RequestParam("closed") boolean closed, Pageable pageable);
}
