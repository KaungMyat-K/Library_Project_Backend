package com.library.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.entity.BookEntity;
import com.library.entity.CheckoutEntity;
import com.library.repository.BookRepository;
import com.library.repository.CheckoutRepository;

@Service
@Transactional
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CheckoutRepository checkoutRepository;


    public BookEntity checkoutBook(String userEmail,Long bookId) throws Exception{
        Optional<BookEntity> book = bookRepository.findById(bookId);
        CheckoutEntity validCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if(!book.isPresent() || validCheckout != null || book.get().getCopiesAvailable() <= 0){
            throw new Exception("Book doesn't exist or already checked out by user");
        }   
        book.get().setCopiesAvailable(book.get().getCopiesAvailable()-1);
        bookRepository.save(book.get());

        CheckoutEntity checkout = CheckoutEntity.builder().userEmail(userEmail).checkoutDate(LocalDate.now().toString()).returnDate(LocalDate.now().plusDays(7).toString()).bookId(book.get().getId()).build();

        checkoutRepository.save(checkout);
        return book.get();
    }

    public Boolean checkoutBookByUser(String userEmail,Long bookId){
        CheckoutEntity validCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if(validCheckout != null){
            return true;
        }else{
            return false;
        }

    }

    public int currentLoansCount(String userEmail){
        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }

}
