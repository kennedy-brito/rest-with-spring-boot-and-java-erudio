package com.kennedy.rest_with_spring_boot_and_java_erudio.commons;

import com.kennedy.rest_with_spring_boot_and_java_erudio.data.vo.v1.PersonVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.model.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonConstants {

    public static Person p1 = new Person(1L, "first name", "last name", "address", "male", true);
    public static PersonVO vo1 = new PersonVO(1L, "first name", "last name", "address", "male", true);

    public static List<Person> getPersonList(){
        List<Person> list = new ArrayList<>();

        for (int i = 0; i < 10; i++){
            Person person = new Person((long) i, "name " + i, "last name " + i, "address " + i, "male", true);
            list.add(person);
        }

        return list;
    }

    public static List<PersonVO> getPersonVOList(){
        List<PersonVO> list = new ArrayList<>();

        for (int i = 0; i < 10; i++){
            PersonVO person = new PersonVO((long) i, "name " + i, "last name " + i, "address " + i, "male", true);
            list.add(person);
        }

        return list;
    }
}
