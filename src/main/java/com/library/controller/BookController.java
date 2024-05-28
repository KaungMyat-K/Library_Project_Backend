package com.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.Utils.ExtractJwt;
import com.library.dao.ShelfCurrentLoansDao;
import com.library.entity.BookEntity;
import com.library.service.BookService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/books")
public class BookController {
     
    @Autowired
    private BookService bookService;

    @Autowired
    private ExtractJwt extractJwt;

    @PutMapping("/secure/checkout")
    public BookEntity checkoutBook(@RequestParam Long bookid,@RequestHeader(value="Authorization")String token) throws Exception {
        String userEmail = extractJwt.payloadJWTExtraction(token,"\"sub\"");
        return bookService.checkoutBook(userEmail, bookid);
    }

    @GetMapping("/secure/ischeckout/byuser")
    public Boolean checkoutBookByUser(@RequestParam Long bookId,@RequestHeader(value="Authorization")String token) {
        String userEmail = extractJwt.payloadJWTExtraction(token,"\"sub\"");
        return bookService.checkoutBookByUser(userEmail,bookId);
    }
    
    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(@RequestHeader(value="Authorization")String token) {
        String userEmail = extractJwt.payloadJWTExtraction(token,"\"sub\"");
        return bookService.currentLoansCount(userEmail);
    }

    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansDao> currentLoans(@RequestHeader(value = "Authorization") String token)
        throws Exception
    {
        String userEmail = extractJwt.payloadJWTExtraction(token, "\"sub\"");
        return bookService.currentLoans(userEmail);
    }
}
