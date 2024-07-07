package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.GoodsReceiptPageRequest;
import org.dainn.dainninventory.controller.response.PageResponse;
import org.dainn.dainninventory.dto.GoodsReceiptDTO;
import org.dainn.dainninventory.service.IGoodsReceiptService;
import org.dainn.dainninventory.utils.ValidateString;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods-receipts")
@RequiredArgsConstructor
public class GoodsReceiptController {
    private final IGoodsReceiptService goodReceiptService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<?> getAll(@ModelAttribute GoodsReceiptPageRequest request) {
        request.setKeyword(ValidateString.trimString(request.getKeyword()));
        if (request.getPage() == null) {
            return ResponseEntity.ok(goodReceiptService.findAll());
        }
        Page<GoodsReceiptDTO> page = goodReceiptService.findWithSpec(request);
        return ResponseEntity.ok(PageResponse.<GoodsReceiptDTO>builder()
                .page(page.getPageable().getPageNumber())
                .size(page.getPageable().getPageSize())
                .totalPages(page.getTotalPages())
                .data(page.getContent())
                .build());
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<?> get(@Min(1) @PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(goodReceiptService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<?> create(@Valid @RequestBody GoodsReceiptDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(goodReceiptService.insert(dto));
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<?> update(@Min(1) @PathVariable(name = "id") Integer id,
                                    @Valid @RequestBody GoodsReceiptDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(goodReceiptService.update(dto));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        goodReceiptService.delete(ids);
        return ResponseEntity.ok("Delete Successfully");
    }
}
