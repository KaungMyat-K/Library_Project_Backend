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
import com.library.entity.HistoryEntity;
import com.library.repository.BookRepository;
import com.library.repository.CheckoutRepository;
import com.library.repository.HistoryRepository;

@Service
@Transactional
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CheckoutRepository checkoutRepository;

    @Autowired
    private HistoryRepository historyRepository;


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

    public void returnBook (String userEmail, Long bookId) throws Exception {

        Optional<BookEntity> book = bookRepository.findById(bookId);

        CheckoutEntity validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (!book.isPresent() || validateCheckout == null) {
            throw new Exception("Book does not exist or not checked out by user");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);

        bookRepository.save(book.get());
        checkoutRepository.deleteById(validateCheckout.getId());

        HistoryEntity history = HistoryEntity.builder().userEmail(userEmail).checkoutDate(validateCheckout.getCheckoutDate()).returnedDate(LocalDate.now().toString()).title(book.get().getTitle()).author(book.get().getAuthor()).description(book.get().getDescription()).img(book.get().getImage()).build();

        historyRepository.save(history);
    }

    public void renewLoan(String userEmail, Long bookId) throws Exception {

        CheckoutEntity validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (validateCheckout == null) {
            throw new Exception("Book does not exist or not checked out by user");
        }

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = sdFormat.parse(validateCheckout.getReturnDate());
        Date d2 = sdFormat.parse(LocalDate.now().toString());

        if (d1.compareTo(d2) > 0 || d1.compareTo(d2) == 0) {
            validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
            checkoutRepository.save(validateCheckout);
        }
    }
}
