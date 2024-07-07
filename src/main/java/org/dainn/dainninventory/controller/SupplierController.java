package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.SupplierPageRequest;
import org.dainn.dainninventory.controller.response.PageResponse;
import org.dainn.dainninventory.dto.SupplierDTO;
import org.dainn.dainninventory.service.ISupplierService;
import org.dainn.dainninventory.utils.ValidateString;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final ISupplierService supplierService;

    @GetMapping
    public ResponseEntity<?> getAll(@ModelAttribute SupplierPageRequest request) {
        request.setKeyword(ValidateString.trimString(request.getKeyword()));
        if (request.getPage() == null) {
            return ResponseEntity.ok(supplierService.findAll(request.getStatus()));
        }
        Page<SupplierDTO> page = supplierService.findAllByName(request);

        return ResponseEntity.ok(PageResponse.<SupplierDTO>builder()
                .page(page.getPageable().getPageNumber())
                .size(page.getPageable().getPageSize())
                .totalPages(page.getTotalPages())
                .data(page.getContent())
                .build());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> get(@Min(1) @PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(supplierService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody SupplierDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.insert(dto));
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable(name = "id") Integer id, @Valid @RequestBody SupplierDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(supplierService.update(dto));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        supplierService.delete(ids);
        return ResponseEntity.ok("Delete Successfully");
    }
}
