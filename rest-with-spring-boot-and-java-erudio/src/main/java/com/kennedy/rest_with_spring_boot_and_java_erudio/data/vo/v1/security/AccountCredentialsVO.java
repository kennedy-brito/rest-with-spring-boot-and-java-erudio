package com.kennedy.rest_with_spring_boot_and_java_erudio.data.vo.v1.security;

import java.io.Serializable;
import java.util.Objects;

public class AccountCredentialsVO implements Serializable{

    private String username;
    private String password;

    public AccountCredentialsVO() {
    }

    public AccountCredentialsVO(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AccountCredentialsVO that)) return false;
        return Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword());
    }
}
