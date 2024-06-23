package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.BrandPageRequest;
import org.dainn.dainninventory.controller.request.RolePageRequest;
import org.dainn.dainninventory.controller.response.PageResponse;
import org.dainn.dainninventory.dto.BrandDTO;
import org.dainn.dainninventory.dto.RoleDTO;
import org.dainn.dainninventory.service.IBrandService;
import org.dainn.dainninventory.utils.ValidateString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<?> getAll(@ModelAttribute BrandPageRequest request) {
        request.setKeyword(ValidateString.trimString(request.getKeyword()));
        if (request.getPage() == null) {
            return ResponseEntity.ok(brandService.findAll(request.getStatus()));
        }
        Page<BrandDTO> page = brandService.findAllByName(request);

        return ResponseEntity.ok(PageResponse.<BrandDTO>builder()
                .page(page.getPageable().getPageNumber())
                .size(page.getPageable().getPageSize())
                .totalPages(page.getTotalPages())
                .data(page.getContent())
                .build());
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
