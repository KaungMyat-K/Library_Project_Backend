package com.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.entity.BookEntity;
import com.library.service.BookService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/books")
public class BookController {
     
    @Autowired
    private BookService bookService;

    @PutMapping("/secure/checkout")
    public BookEntity checkoutBook(@RequestParam Long bookid) throws Exception {
        String userEmail = "aa@gmail.com";
        return bookService.checkoutBook(userEmail, bookid);
    }

    @GetMapping("/secure/ischeckout/byuser")
    public Boolean checkoutBookByUser(@RequestParam Long bookId) {
        String userEmail = "aa@gmail.com";
        return bookService.checkoutBookByUser(userEmail,bookId);
    }
    
    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(@RequestParam Long bookId) {
        String userEmail = "aa@gmail.com";
        return bookService.currentLoansCount(userEmail);
    }
}
