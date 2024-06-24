package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.ProductPageRequest;
import org.dainn.dainninventory.controller.request.ProductRequest;
import org.dainn.dainninventory.controller.response.PageResponse;
import org.dainn.dainninventory.dto.ProductDTO;
import org.dainn.dainninventory.service.IProductService;
import org.dainn.dainninventory.utils.ValidateString;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @GetMapping
    public ResponseEntity<?> getAll(@ModelAttribute ProductPageRequest request) {
        request.setKeyword(ValidateString.trimString(request.getKeyword()));
        if (request.getPage() == null) {
            return ResponseEntity.ok(productService.findAll(request.getStatus()));
        }
        Page<ProductDTO> entityPage = productService.findWithSpec(request);
        return ResponseEntity.ok(PageResponse.<ProductDTO>builder()
                .page(entityPage.getPageable().getPageNumber())
                .size(entityPage.getPageable().getPageSize())
                .totalPages(entityPage.getTotalPages())
                .data(entityPage.getContent())
                .build());
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> get(@PathVariable String code) {
        return ResponseEntity.ok(productService.findByCode(code));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestPart("product") ProductRequest dto,
                                    @Valid @RequestPart("mainImage") MultipartFile mainImg,
                                    @Valid @RequestPart("subImage") List<MultipartFile> subImg) {
        return ResponseEntity.ok(productService.save(dto, mainImg, subImg));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Min(1) @PathVariable Integer id,
                                    @Valid @RequestPart("product") ProductRequest dto,
                                    @RequestPart(value = "mainImage", required = false) MultipartFile mainImg,
                                    @RequestPart(value = "subImage", required = false) List<MultipartFile> subImg) {
        dto.setId(id);
        return ResponseEntity.ok(productService.save(dto, mainImg, subImg));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        productService.delete(ids);
        return ResponseEntity.ok("Delete Successfully");
    }

}
