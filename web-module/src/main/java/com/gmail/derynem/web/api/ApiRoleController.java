package com.gmail.derynem.web.api;

import com.gmail.derynem.service.RoleService;
import com.gmail.derynem.service.model.role.RoleDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ApiRoleController {
    private final RoleService roleService;

    public ApiRoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDTO>> getRoles() {
        List<RoleDTO> roleDTOS = roleService.getRoles();
        return new ResponseEntity<>(roleDTOS, HttpStatus.OK);
    }
}