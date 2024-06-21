package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
    private final IBrandService brandService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(brandService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> get(@Min(1) @PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(brandService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody BrandDTO brandDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(brandService.save(brandDTO));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@Min(1) @PathVariable(name = "id") Integer id,
                                    @Valid @RequestBody BrandDTO brandDTO) {
        brandDTO.setId(id);
        return ResponseEntity.ok(brandService.save(brandDTO));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        brandService.delete(ids);
        return ResponseEntity.ok("Delete Successfully");
    }
}
