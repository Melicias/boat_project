package com.example.demo.dto;

public class BoatResponse {

    private Long id;
    private String name;
    private String description;
    private String user_email;

    public BoatResponse(Long id, String name, String description, String user_email) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user_email = user_email;
    }

    public BoatResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}
