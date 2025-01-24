package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.config.endpoint.Endpoint;
import org.dainn.dainninventory.controller.request.SizePageRequest;
import org.dainn.dainninventory.dto.SizeDTO;
import org.dainn.dainninventory.service.ISizeService;
import org.dainn.dainninventory.utils.ValidateString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Endpoint.Size.BASE)
@RequiredArgsConstructor
public class SizeController {
    private final ISizeService sizeService;


    @GetMapping
    public ResponseEntity<?> getAll(@ModelAttribute SizePageRequest request) {
        request.setKeyword(ValidateString.trimString(request.getKeyword()));
        if (request.getPage() == null) {
            return ResponseEntity.ok(sizeService.findAll(request.getStatus()));
        }
//        Page<BrandDTO> page = sizeService.findAllByName(request);

        return ResponseEntity.ok("");
    }

    @GetMapping(Endpoint.Size.ID)
    public ResponseEntity<?> get(@Min(1) @PathVariable Integer id) {
        return ResponseEntity.ok(sizeService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody SizeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sizeService.insert(dto));
    }

    @PutMapping(Endpoint.Size.ID)
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
