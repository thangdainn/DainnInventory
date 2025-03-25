package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.config.endpoint.Endpoint;
import org.dainn.dainninventory.dto.product.ProductPageRequest;
import org.dainn.dainninventory.dto.product.ProductRequest;
import org.dainn.dainninventory.dto.response.PageResponse;
import org.dainn.dainninventory.dto.product.ProductSizeDTO;
import org.dainn.dainninventory.dto.product.ProductDTO;
import org.dainn.dainninventory.service.IProductService;
import org.dainn.dainninventory.service.IProductSizeService;
import org.dainn.dainninventory.utils.ValidateString;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Endpoint.Product.BASE)
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    private final IProductSizeService productSizeService;

    @GetMapping
    public ResponseEntity<?> getAll(@ModelAttribute ProductPageRequest request) {
        request.setKeyword(ValidateString.trimString(request.getKeyword()));
        if (request.getPage() == null) {
            return ResponseEntity.ok(productService.findAll(request.getStatus()));
        }
        Page<ProductDTO> page = request.getIsStock() ? productService.findInventoryWithSpec(request) : productService.findWithSpec(request);
        return ResponseEntity.ok(PageResponse.<ProductDTO>builder()
                .page(page.getPageable().getPageNumber())
                .size(page.getPageable().getPageSize())
                .totalElements(page.getTotalElements())
                .data(page.getContent())
                .build());
    }

    @GetMapping(Endpoint.Product.CODE)
    public ResponseEntity<?> get(@PathVariable String code) {
        return ResponseEntity.ok(productService.findByCode(code));
    }

    @GetMapping(Endpoint.Product.STOCK)
    public ResponseEntity<?> getQuantity(@RequestParam(name = "productId") Integer productId, @RequestParam(name = "sizeId") Integer sizeId) {
        return ResponseEntity.ok(productSizeService.findByProductIdAndSizeId(productId, sizeId));
    }

    @GetMapping(Endpoint.Product.STOCK_CODE)
    public ResponseEntity<?> getAllByProductCode(@PathVariable(name = "code") String code) {
        return ResponseEntity.ok(productSizeService.findAllByProductCode(code));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<?> create(@Valid @RequestBody ProductRequest dto) {
        return ResponseEntity.ok(productService.insert(dto));
    }

    @PostMapping(Endpoint.Product.ATTRIBUTES)
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<?> createAttributes(@RequestBody List<ProductSizeDTO> list) {
        productSizeService.save(list);
        return ResponseEntity.ok().build();
    }

    @PutMapping(Endpoint.Product.ID)
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<?> update(@Min(1) @PathVariable Integer id,
                                    @Valid @RequestBody ProductRequest dto) {
        dto.setId(id);
        return ResponseEntity.ok(productService.update(dto));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        productService.delete(ids);
        return ResponseEntity.ok().build();
    }

}
