package com.kennedy.rest_with_spring_boot_and_java_erudio.unittests.mockito.services;


import com.kennedy.rest_with_spring_boot_and_java_erudio.data.vo.v1.PersonVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.exceptions.RequiredObjectIsNullException;
import com.kennedy.rest_with_spring_boot_and_java_erudio.model.Person;
import com.kennedy.rest_with_spring_boot_and_java_erudio.repositories.PersonRepository;
import com.kennedy.rest_with_spring_boot_and_java_erudio.services.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static com.kennedy.rest_with_spring_boot_and_java_erudio.commons.PersonConstants.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    PersonRepository repository;

    @BeforeEach
    void setUpMocks(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById(){
        when(repository.findById(p1.getId())).thenReturn(Optional.of(p1));

        var result = personService.findById(p1.getId());

        assertThat(result).isNotNull();
        assertThat(result.getKey()).isNotNull();
        assertThat(result.getLinks()).isNotNull();
        assertThat(result.toString()).contains("[</api/person/v1/1>;rel=\"self\"]");
        assertThat(result.getAddress()).isEqualTo(p1.getAddress());
        assertThat(result.getFirstName()).isEqualTo(p1.getFirstName());
        assertThat(result.getLastName()).isEqualTo(p1.getLastName());
        assertThat(result.getGender()).isEqualTo(p1.getGender());
        assertThat(result.getEnabled()).isEqualTo(p1.getEnabled());
    }

    @Test
    void testCreate(){
        when(repository.save(any(Person.class))).thenReturn(p1);

        var result = personService.create(vo1);

        assertThat(result).isNotNull();
        assertThat(result.getKey()).isNotNull();
        assertThat(result.getLinks()).isNotNull();
        assertThat(result.toString()).contains("[</api/person/v1/1>;rel=\"self\"]");
        assertThat(result.getAddress()).isEqualTo(p1.getAddress());
        assertThat(result.getFirstName()).isEqualTo(p1.getFirstName());
        assertThat(result.getLastName()).isEqualTo(p1.getLastName());
        assertThat(result.getGender()).isEqualTo(p1.getGender());
        assertThat(result.getEnabled()).isEqualTo(p1.getEnabled());
    }

    @Test
    void testCreate_WithNullPerson(){

        String expectedMessage = "It is not allowed to persist a null object!";
        assertThatThrownBy(() -> personService.create(null))
                .isInstanceOf(RequiredObjectIsNullException.class)
                .hasMessage(expectedMessage);
    }

    @Test
    void testUpdate(){
        when(repository.save(any(Person.class))).thenReturn(p1);
        when(repository.findById(p1.getId())).thenReturn(Optional.of(p1));

        var result = personService.update(vo1);

        assertThat(result).isNotNull();
        assertThat(result.getKey()).isNotNull();
        assertThat(result.getLinks()).isNotNull();
        assertThat(result.toString()).contains("[</api/person/v1/1>;rel=\"self\"]");
        assertThat(result.getAddress()).isEqualTo(p1.getAddress());
        assertThat(result.getFirstName()).isEqualTo(p1.getFirstName());
        assertThat(result.getLastName()).isEqualTo(p1.getLastName());
        assertThat(result.getGender()).isEqualTo(p1.getGender());
        assertThat(result.getEnabled()).isEqualTo(p1.getEnabled());
    }

    @Test
    void testUpdate_WithNullPerson(){

        String expectedMessage = "It is not allowed to persist a null object!";
        assertThatThrownBy(() -> personService.update(null))
                .isInstanceOf(RequiredObjectIsNullException.class)
                .hasMessage(expectedMessage);
    }

    @Test
    void testDelete(){
        doNothing().when(repository).delete(any(Person.class));
        when(repository.findById(p1.getId())).thenReturn(Optional.of(p1));

        assertThatNoException().isThrownBy(
                () -> personService.delete(p1.getId())
        );

    }

    @Test
    void testFindAll(){
        List<Person> list = getPersonList();

        when(repository.findAll()).thenReturn(list);

        List<PersonVO> vos = personService.findAll();
        assertThat(vos.size()).isEqualTo(10);

        PersonVO vo = vos.get(0);
        Person p = list.get(0);

        assertThat(vo).isNotNull();
        assertThat(vo.getKey()).isNotNull();
        assertThat(vo.getLinks()).isNotNull();
        assertThat(vo.toString()).contains("[</api/person/v1/0>;rel=\"self\"]");
        assertThat(vo.getAddress()).isEqualTo(p.getAddress());
        assertThat(vo.getFirstName()).isEqualTo(p.getFirstName());
        assertThat(vo.getLastName()).isEqualTo(p.getLastName());
        assertThat(vo.getGender()).isEqualTo(p.getGender());
        assertThat(vo.getEnabled()).isEqualTo(p.getEnabled());

        vo = vos.get(3);
        p = list.get(3);

        assertThat(vo).isNotNull();
        assertThat(vo.getKey()).isNotNull();
        assertThat(vo.getLinks()).isNotNull();
        assertThat(vo.toString()).contains("[</api/person/v1/3>;rel=\"self\"]");
        assertThat(vo.getAddress()).isEqualTo(p.getAddress());
        assertThat(vo.getFirstName()).isEqualTo(p.getFirstName());
        assertThat(vo.getLastName()).isEqualTo(p.getLastName());
        assertThat(vo.getGender()).isEqualTo(p.getGender());
        assertThat(vo.getEnabled()).isEqualTo(p.getEnabled());

        vo = vos.get(7);
        p = list.get(7);

        assertThat(vo).isNotNull();
        assertThat(vo.getKey()).isNotNull();
        assertThat(vo.getLinks()).isNotNull();
        assertThat(vo.toString()).contains("[</api/person/v1/7>;rel=\"self\"]");
        assertThat(vo.getAddress()).isEqualTo(p.getAddress());
        assertThat(vo.getFirstName()).isEqualTo(p.getFirstName());
        assertThat(vo.getLastName()).isEqualTo(p.getLastName());
        assertThat(vo.getGender()).isEqualTo(p.getGender());
        assertThat(vo.getEnabled()).isEqualTo(p.getEnabled());


    }
}
