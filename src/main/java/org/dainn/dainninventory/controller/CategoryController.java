package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.CategoryDTO;
import org.dainn.dainninventory.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> get(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CategoryDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Integer id, @Valid @RequestBody CategoryDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        dto.setId(id);
        return ResponseEntity.ok(categoryService.save(dto));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        categoryService.delete(ids);
        return ResponseEntity.ok("Delete Successfully");
    }
}
