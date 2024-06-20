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
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping(value = "/{name}")
    public ResponseEntity<?> get(@PathVariable(name = "name") String name) {
        return ResponseEntity.ok(roleService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody RoleDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.save(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Integer id, @Valid @RequestBody RoleDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        dto.setId(id);
        return ResponseEntity.ok(roleService.save(dto));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        roleService.delete(ids);
        return ResponseEntity.ok("Delete Successfully");
    }
}
