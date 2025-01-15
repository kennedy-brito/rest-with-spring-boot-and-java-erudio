package com.kennedy.rest_with_spring_boot_and_java_erudio.unittests.mockito.services;


import com.kennedy.rest_with_spring_boot_and_java_erudio.data.vo.v1.BookVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.exceptions.RequiredObjectIsNullException;
import com.kennedy.rest_with_spring_boot_and_java_erudio.model.Book;
import com.kennedy.rest_with_spring_boot_and_java_erudio.repositories.BookRepository;
import com.kennedy.rest_with_spring_boot_and_java_erudio.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.kennedy.rest_with_spring_boot_and_java_erudio.commons.BookConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    BookRepository repository;

    @BeforeEach
    void setUpMocks(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById(){
        when(repository.findById(b1.getId())).thenReturn(Optional.of(b1));

        var result = bookService.findById(b1.getId());

        assertThat(result).isNotNull();
        assertThat(result.getKey()).isNotNull();
        assertThat(result.getLinks()).isNotNull();
        assertThat(result.toString()).contains("[</api/book/v1/1>;rel=\"self\"]");
        assertThat(result.getTitle()).isEqualTo(b1.getTitle());
        assertThat(result.getAuthor()).isEqualTo(b1.getAuthor());
        assertThat(result.getLaunchDate()).isEqualTo(b1.getLaunchDate());
        assertThat(result.getPrice()).isEqualTo(b1.getPrice());
    }

    @Test
    void testCreate(){
        when(repository.save(any(Book.class))).thenReturn(b1);

        var result = bookService.create(vo1);

        assertThat(result).isNotNull();
        assertThat(result.getKey()).isNotNull();
        assertThat(result.getLinks()).isNotNull();
        assertThat(result.toString()).contains("[</api/book/v1/1>;rel=\"self\"]");
        assertThat(result.getTitle()).isEqualTo(b1.getTitle());
        assertThat(result.getAuthor()).isEqualTo(b1.getAuthor());
        assertThat(result.getLaunchDate()).isEqualTo(b1.getLaunchDate());
        assertThat(result.getPrice()).isEqualTo(b1.getPrice());
    }

    @Test
    void testCreate_WithNullBook(){

        String expectedMessage = "It is not allowed to persist a null object!";
        assertThatThrownBy(() -> bookService.create(null))
                .isInstanceOf(RequiredObjectIsNullException.class)
                .hasMessage(expectedMessage);
    }

    @Test
    void testUpdate(){
        when(repository.save(any(Book.class))).thenReturn(b1);
        when(repository.findById(b1.getId())).thenReturn(Optional.of(b1));

        var result = bookService.update(vo1);

        assertThat(result).isNotNull();
        assertThat(result.getKey()).isNotNull();
        assertThat(result.getLinks()).isNotNull();
        assertThat(result.toString()).contains("[</api/book/v1/1>;rel=\"self\"]");
        assertThat(result.getTitle()).isEqualTo(b1.getTitle());
        assertThat(result.getAuthor()).isEqualTo(b1.getAuthor());
        assertThat(result.getLaunchDate()).isEqualTo(b1.getLaunchDate());
        assertThat(result.getPrice()).isEqualTo(b1.getPrice());
    }

    @Test
    void testUpdate_WithNullBook(){

        String expectedMessage = "It is not allowed to persist a null object!";
        assertThatThrownBy(() -> bookService.update(null))
                .isInstanceOf(RequiredObjectIsNullException.class)
                .hasMessage(expectedMessage);
    }

    @Test
    void testDelete(){
        doNothing().when(repository).delete(any(Book.class));
        when(repository.findById(b1.getId())).thenReturn(Optional.of(b1));

        assertThatNoException().isThrownBy(
                () -> bookService.delete(b1.getId())
        );

    }

    @Test
    void testFindAll(){
        List<Book> list = getBookList();

        when(repository.findAll()).thenReturn(list);

        List<BookVO> vos = bookService.findAll();
        assertThat(vos.size()).isEqualTo(10);

        BookVO vo = vos.get(0);
        Book b = list.get(0);

        assertThat(vo).isNotNull();
        assertThat(vo.getKey()).isNotNull();
        assertThat(vo.getLinks()).isNotNull();
        assertThat(vo.toString()).contains("[</api/book/v1/0>;rel=\"self\"]");
        assertThat(vo.getTitle()).isEqualTo(b.getTitle());
        assertThat(vo.getAuthor()).isEqualTo(b.getAuthor());
        assertThat(vo.getLaunchDate()).isEqualTo(b.getLaunchDate());
        assertThat(vo.getPrice()).isEqualTo(b.getPrice());

        vo = vos.get(3);
        b = list.get(3);

        assertThat(vo).isNotNull();
        assertThat(vo.getKey()).isNotNull();
        assertThat(vo.getLinks()).isNotNull();
        assertThat(vo.toString()).contains("[</api/book/v1/3>;rel=\"self\"]");
        assertThat(vo.getTitle()).isEqualTo(b.getTitle());
        assertThat(vo.getAuthor()).isEqualTo(b.getAuthor());
        assertThat(vo.getLaunchDate()).isEqualTo(b.getLaunchDate());
        assertThat(vo.getPrice()).isEqualTo(b.getPrice());

        vo = vos.get(7);
        b = list.get(7);

        assertThat(vo).isNotNull();
        assertThat(vo.getKey()).isNotNull();
        assertThat(vo.getLinks()).isNotNull();
        assertThat(vo.toString()).contains("[</api/book/v1/7>;rel=\"self\"]");
        assertThat(vo.getTitle()).isEqualTo(b.getTitle());
        assertThat(vo.getAuthor()).isEqualTo(b.getAuthor());
        assertThat(vo.getLaunchDate()).isEqualTo(b.getLaunchDate());
        assertThat(vo.getPrice()).isEqualTo(b.getPrice());


    }
}
