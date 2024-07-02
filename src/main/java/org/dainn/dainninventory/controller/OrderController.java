package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.OrderPageRequest;
import org.dainn.dainninventory.controller.request.OrderStatusRequest;
import org.dainn.dainninventory.controller.response.PageResponse;
import org.dainn.dainninventory.dto.OrderDTO;
import org.dainn.dainninventory.service.IOrderService;
import org.dainn.dainninventory.utils.ValidateString;
import org.dainn.dainninventory.utils.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @GetMapping
    public ResponseEntity<?> getAll(@ModelAttribute OrderPageRequest request) {
        request.setKeyword(ValidateString.trimString(request.getKeyword()));
        if (request.getPage() == null) {
            return ResponseEntity.ok(orderService.findAll());
        }
        Page<OrderDTO> page = orderService.findWithSpec(request);
        return ResponseEntity.ok(PageResponse.<OrderDTO>builder()
                .page(page.getPageable().getPageNumber())
                .size(page.getPageable().getPageSize())
                .totalPages(page.getTotalPages())
                .data(page.getContent())
                .build());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> get(@Min(1) @PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

//    @PostMapping
//    public ResponseEntity<?> create(@Valid @RequestBody OrderDTO dto) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.insert(dto));
//    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@Min(1) @PathVariable(name = "id") Integer id,
                                    @Valid @RequestBody OrderStatusRequest orderStatus) {
        return ResponseEntity.ok(orderService.updateStatus(id, orderStatus.getStatus()) == 1 ? "Update success" : "Update failed");
    }

    @DeleteMapping()
    public ResponseEntity<?> delete(@Valid @RequestBody Integer orderId) {
        return ResponseEntity.ok(orderService.updateStatus(orderId, OrderStatus.CANCELLED) == 1 ? "Delete success" : "Delete failed");
    }
}
