package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.config.endpoint.Endpoint;
import org.dainn.dainninventory.config.security.CustomUserDetail;
import org.dainn.dainninventory.dto.order.MyOrderPageRequest;
import org.dainn.dainninventory.dto.order.OrderPageRequest;
import org.dainn.dainninventory.dto.order.OrderStatusRequest;
import org.dainn.dainninventory.dto.response.PageResponse;
import org.dainn.dainninventory.dto.order.MyOrderDTO;
import org.dainn.dainninventory.dto.order.OrderDTO;
import org.dainn.dainninventory.dto.statistic.AnalyticDTO;
import org.dainn.dainninventory.service.IOrderService;
import org.dainn.dainninventory.utils.ValidateString;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoint.Order.BASE)
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
                .totalElements(page.getTotalElements())
                .data(page.getContent())
                .build());
    }

    @GetMapping(Endpoint.Order.ID)
    public ResponseEntity<?> get(@Min(1) @PathVariable Integer id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping(Endpoint.Order.MY_ORDER)
    public ResponseEntity<?> getMyOrder(@ModelAttribute MyOrderPageRequest request, Authentication authentication) {
        request.setKeyword(ValidateString.trimString(request.getKeyword()));
        Integer userId = ((CustomUserDetail) authentication.getPrincipal()).getId();
        request.setUserId(userId);

        Page<MyOrderDTO> page = orderService.findMyOrderWithSpec(request);
        return ResponseEntity.ok(PageResponse.<MyOrderDTO>builder()
                .page(page.getPageable().getPageNumber())
                .size(page.getPageable().getPageSize())
                .totalElements(page.getTotalElements())
                .data(page.getContent())
                .build());
    }

    @GetMapping(Endpoint.Order.PRODUCT)
    public ResponseEntity<?> getByProductId(@Min(1) @PathVariable Integer id, AnalyticDTO request) {
        return ResponseEntity.ok(orderService.findByProductId(id, request.getStartDate(), request.getEndDate()));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody OrderDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.insert(dto));
    }

    @PutMapping(Endpoint.Order.STATUS)
    public ResponseEntity<?> updateStatus(@RequestBody OrderStatusRequest orderStatusChange) {
        orderService.updateStatuses(orderStatusChange.getIds(), orderStatusChange.getStatus());
        return ResponseEntity.ok().build();
    }
}
