package com.gmail.derynem.service.model.role;

public class RoleDTO {
    private Long id;
    private String name; //TODO CHANGE FOR LIST OF PERMISSIONS LATER

    public RoleDTO(long id, String name) {

    }

    public RoleDTO() {
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
