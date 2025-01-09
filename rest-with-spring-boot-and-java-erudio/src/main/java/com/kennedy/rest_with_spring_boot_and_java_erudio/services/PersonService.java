package com.kennedy.rest_with_spring_boot_and_java_erudio.services;
import com.kennedy.rest_with_spring_boot_and_java_erudio.exceptions.ResourceNotFoundException;
import com.kennedy.rest_with_spring_boot_and_java_erudio.model.Person;
import com.kennedy.rest_with_spring_boot_and_java_erudio.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository personRepository;

    public Person create(Person person){

        logger.info("Creating one person!");

        return personRepository.save(person);
    }

    public Person update(Person person){

        logger.info("Updating one person!");

        Person entity = this.findById(person.getId());

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return person;
    }

    public void delete(Long id){

        logger.info("Deleting one person!");

        Person entity = this.findById(id);

        personRepository.delete(entity);
    }

    public Person findById(Long id){

        logger.info("Finding one person!");

        return personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this ID!")
        );
    }

    public List<Person> findAll(){

        List<Person> persons = personRepository.findAll();
        logger.info("Finding all people!");

        return persons;
    }

}
