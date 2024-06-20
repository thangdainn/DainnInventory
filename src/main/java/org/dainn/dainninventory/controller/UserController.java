package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.UserPageRequest;
import org.dainn.dainninventory.controller.request.UserRequest;
import org.dainn.dainninventory.controller.response.PageResponse;
import org.dainn.dainninventory.dto.UserDTO;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.service.IUserService;
import org.dainn.dainninventory.utils.ProviderId;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping
    public ResponseEntity<?> getAll(@ModelAttribute UserPageRequest request) {
        if (request.getPage() == null) {
            return ResponseEntity.ok(userService.findAll());
        }
        Page<UserDTO> entityPage = userService.findAll(request);
        return ResponseEntity.ok(PageResponse.<UserDTO>builder()
                .page(entityPage.getPageable().getPageNumber())
                .size(entityPage.getPageable().getPageSize())
                .totalPages(entityPage.getTotalPages())
                .data(entityPage.getContent())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserRequest dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(userService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody UserRequest dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        dto.setId(id);
        return ResponseEntity.ok(userService.save(dto));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        userService.delete(ids);
        return ResponseEntity.ok("Delete Successfully");
    }
}
