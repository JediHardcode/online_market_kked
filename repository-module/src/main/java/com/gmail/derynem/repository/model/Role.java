package com.gmail.derynem.repository.model;

public class Role {
    private Long id;
    private String name; //TODO CHANGE FOR LIST OF PERMISSIONS LATER

    public Role() {
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
}
