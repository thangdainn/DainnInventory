package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.BrandDTO;
import org.dainn.dainninventory.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    @Autowired
    private IBrandService brandService;

    @GetMapping
    public ResponseEntity<?> getBrands() {
        return ResponseEntity.ok(brandService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getBrand(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(brandService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody BrandDTO brandDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        brandDTO = brandService.save(brandDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(brandDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateRole(@PathVariable(name = "id") Integer id, @Valid @RequestBody BrandDTO brandDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        brandDTO.setId(id);
        brandDTO = brandService.save(brandDTO);
        return ResponseEntity.ok(brandDTO);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRole(@RequestBody List<Integer> ids) {
        brandService.delete(ids);
        return ResponseEntity.ok("Delete Successfully");
    }
}
