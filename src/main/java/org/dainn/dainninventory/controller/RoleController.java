package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.RoleDTO;
import org.dainn.dainninventory.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;

    @GetMapping
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping(value = "/{name}")
    public ResponseEntity<?> getRole(@PathVariable(name = "name") String name) {
        RoleDTO dto = roleService.findByName(name);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleDTO roleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        roleDTO = roleService.save(roleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateRole(@PathVariable(name = "id") Integer id, @Valid @RequestBody RoleDTO roleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        roleDTO.setId(id);
        roleDTO = roleService.save(roleDTO);
        return ResponseEntity.ok(roleDTO);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRole(@RequestBody List<Integer> ids) {
        roleService.delete(ids);
        return ResponseEntity.ok("Delete Successfully");
    }
}
