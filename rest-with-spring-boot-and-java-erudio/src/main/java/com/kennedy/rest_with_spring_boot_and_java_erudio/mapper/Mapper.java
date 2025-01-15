package com.kennedy.rest_with_spring_boot_and_java_erudio.mapper;


import com.kennedy.rest_with_spring_boot_and_java_erudio.data.vo.v1.BookVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.data.vo.v1.PersonVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.model.Book;
import com.kennedy.rest_with_spring_boot_and_java_erudio.model.Person;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

//    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();
    private static final ModelMapper mapper = new ModelMapper();

    static{

        mapper
                .createTypeMap(Person.class, PersonVO.class)
                .addMapping(Person::getId, PersonVO::setKey);

        mapper
                .createTypeMap(PersonVO.class, Person.class)
                .addMapping(PersonVO::getKey, Person::setId);

        mapper
                .createTypeMap(Book.class, BookVO.class)
                .addMapping(Book::getId, BookVO::setKey);

        mapper
                .createTypeMap(BookVO.class, Book.class)
                .addMapping(BookVO::getKey, Book::setId);
    }

    public static <O, D> D parseObject(O origin, Class<D> destination){
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination){

        List<D> destinationObjects = new ArrayList<>();

        origin.forEach(
            (o) -> destinationObjects.add(mapper.map(o, destination))
        );

        return destinationObjects;
    }
}
