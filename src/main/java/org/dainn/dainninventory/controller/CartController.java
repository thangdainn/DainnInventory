package org.dainn.dainninventory.controller;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.CartDTO;
import org.dainn.dainninventory.service.ICartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final ICartService cartService;

    @GetMapping
    public ResponseEntity<?> getAllByUserId(@RequestBody CartDTO dto) {
        if (dto.getUserId() == null) {
            return ResponseEntity.badRequest().body("User Id is required");
        }
        return ResponseEntity.ok(cartService.findAllByUserId(dto.getUserId()));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CartDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.insert(dto));
    }

    @PutMapping()
    public ResponseEntity<?> update(@RequestBody CartDTO dto) {
        return ResponseEntity.ok(cartService.updateCart(dto));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        cartService.deleteCart(ids);
        return ResponseEntity.ok("Delete Successfully");
    }
}
