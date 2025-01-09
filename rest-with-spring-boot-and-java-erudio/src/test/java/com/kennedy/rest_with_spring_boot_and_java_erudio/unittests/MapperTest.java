package com.kennedy.rest_with_spring_boot_and_java_erudio.unittests;

import static com.kennedy.rest_with_spring_boot_and_java_erudio.commons.PersonConstants.*;
import static org.assertj.core.api.Assertions.*;

import com.kennedy.rest_with_spring_boot_and_java_erudio.data.vo.v1.PersonVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.mapper.Mapper;
import com.kennedy.rest_with_spring_boot_and_java_erudio.model.Person;
import org.junit.jupiter.api.Test;

public class MapperTest {

    @Test
    public void parseEntityToVOTest() {

        PersonVO vo = Mapper.parseObject(p1, PersonVO.class);

        assertThat(vo.getId()).isEqualTo(p1.getId());
        assertThat(vo.getFirstName()).isEqualTo(p1.getFirstName());
        assertThat(vo.getLastName()).isEqualTo(p1.getLastName());
        assertThat(vo.getAddress()).isEqualTo(p1.getAddress());
        assertThat(vo.getGender()).isEqualTo(p1.getGender());
    }

    @Test
    public void parseVOToEntityTest() {

        Person p1 = Mapper.parseObject(vo1, Person.class);

        assertThat(vo1.getId()).isEqualTo(p1.getId());
        assertThat(vo1.getFirstName()).isEqualTo(p1.getFirstName());
        assertThat(vo1.getLastName()).isEqualTo(p1.getLastName());
        assertThat(vo1.getAddress()).isEqualTo(p1.getAddress());
        assertThat(vo1.getGender()).isEqualTo(p1.getGender());
    }

    @Test
    public void parseEntityListToVOListTest() {

        getPersonList().forEach(
                (p) -> {
                    PersonVO vo = Mapper.parseObject(p, PersonVO.class);

                    assertThat(vo.getId()).isEqualTo(p.getId());
                    assertThat(vo.getFirstName()).isEqualTo(p.getFirstName());
                    assertThat(vo.getLastName()).isEqualTo(p.getLastName());
                    assertThat(vo.getAddress()).isEqualTo(p.getAddress());
                    assertThat(vo.getGender()).isEqualTo(p.getGender());
                }
        );
    }

    @Test
    public void parserVOListToEntityListTest() {
        getPersonVOList().forEach(
                (vo) -> {
                    Person p = Mapper.parseObject(vo, Person.class);

                    assertThat(vo.getId()).isEqualTo(p.getId());
                    assertThat(vo.getFirstName()).isEqualTo(p.getFirstName());
                    assertThat(vo.getLastName()).isEqualTo(p.getLastName());
                    assertThat(vo.getAddress()).isEqualTo(p.getAddress());
                    assertThat(vo.getGender()).isEqualTo(p.getGender());
                }
        );
    }
}
