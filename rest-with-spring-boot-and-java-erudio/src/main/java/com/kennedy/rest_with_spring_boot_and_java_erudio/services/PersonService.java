package com.kennedy.rest_with_spring_boot_and_java_erudio.services;
import com.kennedy.rest_with_spring_boot_and_java_erudio.controllers.PersonController;
import com.kennedy.rest_with_spring_boot_and_java_erudio.data.vo.v1.PersonVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.exceptions.RequiredObjectIsNullException;
import com.kennedy.rest_with_spring_boot_and_java_erudio.exceptions.ResourceNotFoundException;
import com.kennedy.rest_with_spring_boot_and_java_erudio.mapper.Mapper;
import com.kennedy.rest_with_spring_boot_and_java_erudio.model.Person;
import com.kennedy.rest_with_spring_boot_and_java_erudio.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    PagedResourcesAssembler<PersonVO> assembler;
    public PersonVO create(PersonVO person){

        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one person!");
        Person entity = Mapper.parseObject(person, Person.class);
        entity = personRepository.save(entity);

        PersonVO vo = Mapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO person){

        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one person!");

        Person entity = personRepository.findById(person.getKey()).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this ID!")
        );

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        PersonVO vo = Mapper.parseObject(personRepository.save(entity), PersonVO.class);

        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());

        return vo;
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

        PersonVO vo = Mapper.parseObject(entity, PersonVO.class);

        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());

        return vo;
    }

    public List<PersonVO> findAll(){return null;}
    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable){

        logger.info("Finding all people!");

        Page<Person> personPage = personRepository.findAll(pageable);

        Page<PersonVO> personVosPage = personPage.map(p-> {
            PersonVO vo = Mapper.parseObject(p, PersonVO.class);
            vo.add(
                    linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
            return vo;
        });

        Link link =
                linkTo(
                        methodOn(PersonController.class)
                                .findAll(
                                        pageable.getPageNumber(),
                                        pageable.getPageSize(),
                                        "asc")).withSelfRel();

        return assembler.toModel(personVosPage, link);
    }

    @Transactional
    public PersonVO disablePerson(Long id){

        logger.info("Disabling one person!");

        personRepository.disablePerson(id);

        Person entity = personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this ID!")
        );

        PersonVO vo = Mapper.parseObject(entity, PersonVO.class);

        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());

        return vo;
    }

    public PagedModel<EntityModel<PersonVO>> findPersonsByName(String firstName, Pageable pageable){

        logger.info("Finding people by first name!");

        Page<Person> personPage = personRepository.findPersonsByName(firstName, pageable);

        Page<PersonVO> personVosPage = personPage.map(p-> {
            PersonVO vo = Mapper.parseObject(p, PersonVO.class);
            vo.add(
                    linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
            return vo;
        });

        Link link =
                linkTo(
                        methodOn(PersonController.class)
                                .findPeopleByFirstName(
                                        firstName,
                                        pageable.getPageNumber(),
                                        pageable.getPageSize(),
                                        "asc")).withSelfRel();

        return assembler.toModel(personVosPage, link);
    }

}
