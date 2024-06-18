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
    public ResponseEntity<?> getUsers(@ModelAttribute UserPageRequest request) {
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
    public ResponseEntity<?> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        if (checkEmail(dto.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        return ResponseEntity.ok(userService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @Valid @RequestBody UserRequest dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        if (!userService.findById(id).getEmail().equals(dto.getEmail()) && checkEmail(dto.getEmail())){
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        dto.setId(id);
        return ResponseEntity.ok(userService.save(dto));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestBody List<Integer> ids) {
        userService.delete(ids);
        return ResponseEntity.ok("Delete Successfully");
    }

    private boolean checkEmail(String email) {
        return userService.findByEmailAndProviderId(email, ProviderId.local) != null;
    }
}
