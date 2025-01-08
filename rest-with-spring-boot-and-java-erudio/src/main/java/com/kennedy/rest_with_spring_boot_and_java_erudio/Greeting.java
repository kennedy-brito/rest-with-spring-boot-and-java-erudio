package com.kennedy.rest_with_spring_boot_and_java_erudio;

public class Greeting {

    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        this.content = content;
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public long getId() {
        return id;
    }
}
