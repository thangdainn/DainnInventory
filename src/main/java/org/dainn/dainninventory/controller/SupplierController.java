package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.BrandDTO;
import org.dainn.dainninventory.dto.SupplierDTO;
import org.dainn.dainninventory.service.IBrandService;
import org.dainn.dainninventory.service.ISupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final ISupplierService supplierService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(supplierService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> get(@Min(1) @PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(supplierService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody SupplierDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.save(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Integer id, @Valid @RequestBody SupplierDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(supplierService.save(dto));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        supplierService.delete(ids);
        return ResponseEntity.ok("Delete Successfully");
    }
}
