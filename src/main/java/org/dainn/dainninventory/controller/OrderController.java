package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.OrderStatusRequest;
import org.dainn.dainninventory.dto.OrderDTO;
import org.dainn.dainninventory.service.IOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> get(@Min(1) @PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody OrderDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.insert(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@Min(1) @PathVariable(name = "id") Integer id,
                                    @Valid @RequestBody OrderStatusRequest orderStatus) {
        return ResponseEntity.ok(orderService.updateStatus(id, orderStatus.getStatus()) == 1 ? "Update success" : "Update failed");
    }
}
