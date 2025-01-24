package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.config.endpoint.Endpoint;
import org.dainn.dainninventory.config.security.CustomUserDetail;
import org.dainn.dainninventory.controller.request.CartPageRequest;
import org.dainn.dainninventory.controller.response.PageResponse;
import org.dainn.dainninventory.dto.CartDTO;
import org.dainn.dainninventory.service.ICartService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Endpoint.Cart.BASE)
@RequiredArgsConstructor
public class CartController {
    private final ICartService cartService;

    @GetMapping()
    public ResponseEntity<?> getAllByUserId(@ModelAttribute CartPageRequest request, Authentication authentication) {
        Integer userId = ((CustomUserDetail) authentication.getPrincipal()).getId();
        request.setUserId(userId);

        Page<CartDTO> page = cartService.findAllByUserId(request);
        return ResponseEntity.ok(PageResponse.<CartDTO>builder()
                .page(page.getPageable().getPageNumber())
                .size(page.getPageable().getPageSize())
                .totalElements(page.getTotalElements())
                .data(page.getContent())
                .build());
    }

    @GetMapping(Endpoint.Cart.COUNT)
    public ResponseEntity<?> countByUserId(Authentication authentication) {
        Integer userId = ((CustomUserDetail) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(cartService.countByUserId(userId));
    }


    @PostMapping
    public ResponseEntity<?> creates(@Valid @RequestBody List<CartDTO> dtos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.inserts(dtos));
    }

    @PutMapping()
    public ResponseEntity<?> update(@RequestBody CartDTO dto) {
        return ResponseEntity.ok(cartService.updateCart(dto));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        cartService.deleteCart(ids);
        return ResponseEntity.ok().build();
    }
}
