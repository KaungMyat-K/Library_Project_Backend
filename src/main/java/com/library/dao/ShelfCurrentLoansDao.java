package com.library.dao;

import com.library.entity.BookEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShelfCurrentLoansDao {

    private BookEntity book;
    private int daysLeft;

}
