package com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.controller.withyml.mapper.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class YmlParseException extends RuntimeException {
    public YmlParseException(JsonProcessingException e) {
        super(e);
    }
}
