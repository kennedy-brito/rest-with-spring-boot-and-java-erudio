package com.kennedy.rest_with_spring_boot_and_java_erudio.services;
import com.kennedy.rest_with_spring_boot_and_java_erudio.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PersonService {

    private static final AtomicLong counter = new AtomicLong();
    private static final Logger logger = LoggerFactory.getLogger(PersonService.class.getName());

    public Person create(Person person){

        logger.info("Creating one person!");

        return person;
    }

    public Person update(Person person){

        logger.info("Updating one person!");

        return person;
    }

    public void delete(String id){

        logger.info("Deleting one person!");
    }

    public Person findById(String id){

        logger.info("Finding one person!");

        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Willian");
        person.setLastName("Brito");
        person.setAddress("Street xxxx");
        person.setGender("male");
        return person;
    }

    public List<Person> findAll(){

        List<Person> persons = new ArrayList<>();
        logger.info("Finding all people!");

        for (int i = 0; i < 8; i++) {
            persons.add(mockPerson(i));
        }

        return persons;
    }

    private Person mockPerson(int i) {

        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Person name " + i);
            person.setLastName("Last name " + i);
        person.setAddress("Some address in Brasil " + i);
        person.setGender("male " + i);

        return person;
    }
}
