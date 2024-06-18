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

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getBrands() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getBrand(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody CategoryDTO CategoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        CategoryDTO = categoryService.save(CategoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateRole(@PathVariable(name = "id") Integer id, @Valid @RequestBody CategoryDTO CategoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        CategoryDTO.setId(id);
        CategoryDTO = categoryService.save(CategoryDTO);
        return ResponseEntity.ok(CategoryDTO);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRole(@RequestBody List<Integer> ids) {
        categoryService.delete(ids);
        return ResponseEntity.ok("Delete Successfully");
    }
}
