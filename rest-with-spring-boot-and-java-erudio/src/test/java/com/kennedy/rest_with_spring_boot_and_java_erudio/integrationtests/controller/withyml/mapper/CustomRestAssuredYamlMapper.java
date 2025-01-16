package com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.controller.withyml.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.controller.withyml.mapper.exception.YmlParseException;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class CustomRestAssuredYamlMapper implements ObjectMapper {

    private com.fasterxml.jackson.databind.ObjectMapper mapper;

    public CustomRestAssuredYamlMapper(com.fasterxml.jackson.databind.ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Object deserialize(ObjectMapperDeserializationContext context) {
        // devo transformar para um objeto java
        String content = context.getDataToDeserialize().asString();
        Type type = context.getType();
        try {
            String typeName = type.getTypeName();
            Class<?> valueType = Class.forName(typeName);
            return mapper.readValue(content, valueType);
        } catch (JsonProcessingException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object serialize(ObjectMapperSerializationContext context) {
        Object serialize = context.getObjectToSerialize();

        try {
            return mapper.writeValueAsString(serialize);
        } catch (JsonProcessingException e) {
            throw new YmlParseException(e);
        }
    }
}
