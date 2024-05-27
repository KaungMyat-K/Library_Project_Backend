package com.library.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.dao.ShelfCurrentLoansDao;
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

    public List<ShelfCurrentLoansDao> currentLoans(String userEmail) throws Exception {

        List<ShelfCurrentLoansDao> shelfCurrentLoansResponses = new ArrayList<>();

        List<CheckoutEntity> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);
        List<Long> bookIdList = new ArrayList<>();

        for (CheckoutEntity i: checkoutList) {
            bookIdList.add(i.getBookId());
        }

        List<BookEntity> books = bookRepository.findBooksByBookIds(bookIdList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (BookEntity book : books) {
            Optional<CheckoutEntity> checkout = checkoutList.stream()
                    .filter(x -> x.getBookId() == book.getId()).findFirst();

            if (checkout.isPresent()) {

                Date d1 = sdf.parse(checkout.get().getReturnDate());
                Date d2 = sdf.parse(LocalDate.now().toString());

                TimeUnit time = TimeUnit.DAYS;

                long difference_In_Time = time.convert(d1.getTime() - d2.getTime(),
                        TimeUnit.MILLISECONDS);

                shelfCurrentLoansResponses.add(ShelfCurrentLoansDao.builder().book(book).daysLeft((int)difference_In_Time).build());
                
            }
        }
        return shelfCurrentLoansResponses;
    }
}
