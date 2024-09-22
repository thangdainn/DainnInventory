package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.SizePageRequest;
import org.dainn.dainninventory.dto.SizeDTO;
import org.dainn.dainninventory.service.IProductSizeService;
import org.dainn.dainninventory.service.ISizeService;
import org.dainn.dainninventory.utils.ValidateString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sizes")
@RequiredArgsConstructor
public class SizeController {
    private final ISizeService sizeService;
    private final IProductSizeService productSizeService;

    @GetMapping
    public ResponseEntity<?> getAll(@ModelAttribute SizePageRequest request) {
        request.setKeyword(ValidateString.trimString(request.getKeyword()));
        if (request.getPage() == null) {
            return ResponseEntity.ok(sizeService.findAll(request.getStatus()));
        }
//        Page<BrandDTO> page = sizeService.findAllByName(request);

        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/quantity-code")
    public ResponseEntity<?> getAllByProductCode(@RequestParam(name = "code") String code) {
        return ResponseEntity.ok(productSizeService.findAllByProductCode(code));
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<?> get(@Min(1) @PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(sizeService.findById(id));
    }
    @GetMapping(value = "/quantity")
    public ResponseEntity<?> getQuantity(@RequestParam(name = "productId") Integer productId, @RequestParam(name = "sizeId") Integer sizeId) {
        return ResponseEntity.ok(productSizeService.findByProductIdAndSizeId(productId, sizeId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody SizeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sizeService.insert(dto));
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@Min(1) @PathVariable(name = "id") Integer id,
                                    @Valid @RequestBody SizeDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(sizeService.update(dto));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        sizeService.delete(ids);
        return ResponseEntity.ok("Delete Successfully");
    }
}
