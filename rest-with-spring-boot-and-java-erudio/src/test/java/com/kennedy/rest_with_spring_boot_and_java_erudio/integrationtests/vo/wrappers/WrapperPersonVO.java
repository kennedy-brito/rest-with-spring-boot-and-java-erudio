package com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.vo.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Objects;

@XmlRootElement(name = "WrapperPersonVO")
public class WrapperPersonVO implements Serializable {

    @JsonProperty("_embedded")
    private PersonEmbeddedVO embedded;

    public WrapperPersonVO() {
    }

    public PersonEmbeddedVO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(PersonEmbeddedVO embedded) {
        this.embedded = embedded;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WrapperPersonVO that)) return false;
        return Objects.equals(getEmbedded(), that.getEmbedded());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEmbedded());
    }
}
