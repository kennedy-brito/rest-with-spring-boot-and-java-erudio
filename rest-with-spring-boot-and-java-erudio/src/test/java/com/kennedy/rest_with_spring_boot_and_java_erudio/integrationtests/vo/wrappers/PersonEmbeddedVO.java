package com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.vo.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.vo.PersonVO;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;
import java.util.Objects;

@XmlRootElement(name = "PersonEmbeddedVO")
public class PersonEmbeddedVO {

    @JsonProperty("personVOList")
    private List<PersonVO> persons;

    public PersonEmbeddedVO() {
    }

    public List<PersonVO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonVO> persons) {
        this.persons = persons;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PersonEmbeddedVO that)) return false;
        return Objects.equals(getPersons(), that.getPersons());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPersons());
    }
}
