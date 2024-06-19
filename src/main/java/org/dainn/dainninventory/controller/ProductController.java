package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.ProductPageRequest;
import org.dainn.dainninventory.controller.request.ProductRequest;
import org.dainn.dainninventory.controller.request.UserPageRequest;
import org.dainn.dainninventory.controller.request.UserRequest;
import org.dainn.dainninventory.controller.response.PageResponse;
import org.dainn.dainninventory.dto.InventoryDTO;
import org.dainn.dainninventory.dto.ProductDTO;
import org.dainn.dainninventory.dto.UserDTO;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.service.IProductService;
import org.dainn.dainninventory.service.IUserService;
import org.dainn.dainninventory.utils.ProviderId;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
        if (request.getPage() == null) {
            return ResponseEntity.ok(productService.findAll());
        }
        Page<ProductDTO> entityPage = productService.findAll(request);
        return ResponseEntity.ok(PageResponse.<ProductDTO>builder()
                .page(entityPage.getPageable().getPageNumber())
                .size(entityPage.getPageable().getPageSize())
                .totalPages(entityPage.getTotalPages())
                .data(entityPage.getContent())
                .build());
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getProduct(@PathVariable String code) {
        return ResponseEntity.ok(productService.findByCode(code));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestPart("product") ProductRequest dto,
                                    @Valid @RequestPart("mainImage") MultipartFile mainImg,
                                    @Valid @RequestPart("subImage") List<MultipartFile> subImg,
                                    BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(productService.save(dto, mainImg, subImg));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @Valid @RequestPart("product") ProductRequest dto,
                                    @RequestPart(value = "mainImage", required = false) MultipartFile mainImg,
                                    @RequestPart(value = "subImage", required = false) List<MultipartFile> subImg,
                                    BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        dto.setId(id);
        return ResponseEntity.ok(productService.save(dto, mainImg, subImg));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        productService.delete(ids);
        return ResponseEntity.ok("Delete Successfully");
    }

}
