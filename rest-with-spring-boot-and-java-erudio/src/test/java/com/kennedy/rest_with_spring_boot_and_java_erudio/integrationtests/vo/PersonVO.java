package com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Objects;

public class PersonVO implements Serializable {


    private Long id;

    private String firstName;
    private String lastName;
    private String address;
    private String gender;

    public PersonVO() {
    }

    public PersonVO(Long id, String firstName, String lastName, String address, String gender) {
        this.address = address;
        this.firstName = firstName;
        this.gender = gender;
        this.id = id;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PersonVO person)) return false;
        return Objects.equals(id, person.id) && Objects.equals(getFirstName(), person.getFirstName()) && Objects.equals(getLastName(), person.getLastName()) && Objects.equals(getAddress(), person.getAddress()) && Objects.equals(getGender(), person.getGender());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getFirstName(), getLastName(), getAddress(), getGender());
    }
}
