package com.kennedy.rest_with_spring_boot_and_java_erudio.commons;

import com.kennedy.rest_with_spring_boot_and_java_erudio.data.vo.v1.BookVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.model.Book;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookConstants {

    public static Book b1 = new Book(1L, "first name", "last name", new BigDecimal(800), new Date());
    public static BookVO vo1 = new BookVO(1L, "first name", "last name", new BigDecimal(800), new Date());

    public static List<Book> getBookList(){
        List<Book> list = new ArrayList<>();

        for (int i = 0; i < 10; i++){
            Book book = new Book((long) i, "name " + i, "last name " + i, new BigDecimal(800 + i) , new Date(Instant.now().getEpochSecond() + (long) i));
            list.add(book);
        }

        return list;
    }

    public static List<BookVO> getBookVOList(){
        List<BookVO> list = new ArrayList<>();

        for (int i = 0; i < 10; i++){
            BookVO book = new BookVO((long) i, "name " + i, "last name " + i, new BigDecimal(800 + i) , new Date(Instant.now().getEpochSecond() + (long) i));
            list.add(book);
        }

        return list;
    }
}
