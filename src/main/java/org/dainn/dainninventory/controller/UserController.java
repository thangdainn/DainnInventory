package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.UserPageRequest;
import org.dainn.dainninventory.controller.response.PageResponse;
import org.dainn.dainninventory.controller.response.UserResponse;
import org.dainn.dainninventory.dto.UserDTO;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.service.IUserService;
import org.dainn.dainninventory.utils.ProviderId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping
    public ResponseEntity<?> getUsers(@ModelAttribute UserPageRequest request) {
        if (request.getPage() == null) {
            return ResponseEntity.ok(userService.findAll());
        }
        Sort sort;
        if (request.getSortBy() != null && !request.getSortBy().isBlank()) {
            sort = request.getSortBy().equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(request.getSortBy()).ascending() : Sort.by(request.getSortBy()).descending();
        } else {
            sort = Sort.unsorted();
        }
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<UserResponse> entityPage;
        if (request.getKeyword() != null) {
            entityPage = userService.findByEmailContaining(request.getKeyword(), pageable);
        } else {
            entityPage = userService.findAll(pageable);
        }

        return ResponseEntity.ok(PageResponse.<UserResponse>builder()
                .page(entityPage.getPageable().getPageNumber())
                .size(entityPage.getPageable().getPageSize())
                .totalPages(entityPage.getTotalPages())
                .data(entityPage.getContent())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        if (checkEmail(dto.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        return ResponseEntity.ok(userService.save(dto));
    }

    private boolean checkEmail(String email) {
        return userService.findByEmailAndProviderId(email, ProviderId.local) != null;
    }
}
