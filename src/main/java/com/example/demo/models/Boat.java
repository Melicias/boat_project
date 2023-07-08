package com.example.demo.models;

import jakarta.persistence.*;

@Entity
@Table
public class Boat {
    @Id
    @SequenceGenerator(
            name = "boat_sequence",
            sequenceName = "boat_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "boat_sequence"
    )
    private Long id;
    private String name;
    private String description;

    public Boat(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Boat(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Boat(){

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

    @Override
    public String toString() {
        return "Boat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}