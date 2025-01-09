package com.kennedy.rest_with_spring_boot_and_java_erudio.repositories;

import com.kennedy.rest_with_spring_boot_and_java_erudio.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
