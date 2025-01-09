package com.kennedy.rest_with_spring_boot_and_java_erudio.services;
import com.kennedy.rest_with_spring_boot_and_java_erudio.data.vo.v1.PersonVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.exceptions.ResourceNotFoundException;
import com.kennedy.rest_with_spring_boot_and_java_erudio.mapper.Mapper;
import com.kennedy.rest_with_spring_boot_and_java_erudio.model.Person;
import com.kennedy.rest_with_spring_boot_and_java_erudio.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository personRepository;

    public PersonVO create(PersonVO person){

        logger.info("Creating one person!");
        Person entity = Mapper.parseObject(person, Person.class);
        entity = personRepository.save(entity);

        PersonVO vo = Mapper.parseObject(entity, PersonVO.class);

        return vo;
    }

    public PersonVO update(PersonVO person){

        logger.info("Updating one person!");

        Person entity = personRepository.findById(person.getId()).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this ID!")
        );

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        PersonVO vo = Mapper.parseObject(personRepository.save(entity), PersonVO.class);

        return person;
    }

    public void delete(Long id){

        logger.info("Deleting one person!");

        Person entity = personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this ID!")
        );

        personRepository.delete(entity);
    }

    public PersonVO findById(Long id){

        logger.info("Finding one person!");

        Person entity = personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this ID!")
        );

        return Mapper.parseObject(entity, PersonVO.class);
    }

    public List<PersonVO> findAll(){

        List<Person> persons = personRepository.findAll();
        logger.info("Finding all people!");

        return Mapper.parseListObjects(persons, PersonVO.class);
    }

}
