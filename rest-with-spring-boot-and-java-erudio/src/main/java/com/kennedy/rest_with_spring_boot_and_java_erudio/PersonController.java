package com.kennedy.rest_with_spring_boot_and_java_erudio;

import com.kennedy.rest_with_spring_boot_and_java_erudio.data.vo.v1.PersonVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kennedy.rest_with_spring_boot_and_java_erudio.util.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/person/v1")
@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}
    )
    public PersonVO findById(@PathVariable Long id) {
        return personService.findById(id);
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}
    )
    public PersonVO create(@RequestBody PersonVO person) {
        return personService.create(person);
    }

    @PutMapping(
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}
    )
    public PersonVO update(@RequestBody PersonVO person) {
        return personService.update(person);
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}
    )
    public List<PersonVO> findAll() {
        return personService.findAll();
    }

    @DeleteMapping(
            value = "/{id}"
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
