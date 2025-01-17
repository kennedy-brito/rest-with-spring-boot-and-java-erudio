package com.kennedy.rest_with_spring_boot_and_java_erudio.services;

import com.kennedy.rest_with_spring_boot_and_java_erudio.controllers.BookController;
import com.kennedy.rest_with_spring_boot_and_java_erudio.data.vo.v1.BookVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.exceptions.RequiredObjectIsNullException;
import com.kennedy.rest_with_spring_boot_and_java_erudio.exceptions.ResourceNotFoundException;
import com.kennedy.rest_with_spring_boot_and_java_erudio.mapper.Mapper;
import com.kennedy.rest_with_spring_boot_and_java_erudio.model.Book;
import com.kennedy.rest_with_spring_boot_and_java_erudio.repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    @Autowired
    private BookRepository repository;

    public BookVO create(BookVO entity){
        if (entity == null) throw new RequiredObjectIsNullException();

        log.info("Creating a Book!");

        Book book = Mapper.parseObject(entity, Book.class);

        book = repository.save(book);

        BookVO vo = Mapper.parseObject(book, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO findById(Long id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        BookVO vo = Mapper.parseObject(book, BookVO.class);

        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }

    public BookVO update(BookVO entity) {
        if (entity == null) throw new RequiredObjectIsNullException();

        log.info("Updatin a Book!");

        Book book = repository.findById(entity.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        book.setAuthor(entity.getAuthor());
        book.setPrice(entity.getPrice());
        book.setTitle(entity.getTitle());
        book.setLaunchDate(entity.getLaunchDate());

        book = repository.save(book);

        BookVO vo = Mapper.parseObject(book, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }

    public void delete(Long id) {
        Book entity=  repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        repository.delete(entity);
    }

    public Page<BookVO> findAll(Pageable pageable) {
        Page<Book> books = repository.findAll(pageable);

        Page<BookVO> vos = books.map(b ->{
            BookVO vo = Mapper.parseObject(b, BookVO.class);

            vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());

            return vo;
        });

        return vos;
    }
}
