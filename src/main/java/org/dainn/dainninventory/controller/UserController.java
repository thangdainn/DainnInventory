package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.UserPageRequest;
import org.dainn.dainninventory.controller.request.UserRequest;
import org.dainn.dainninventory.controller.response.PageResponse;
import org.dainn.dainninventory.dto.UserDTO;
import org.dainn.dainninventory.service.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<?> getAll(@ModelAttribute UserPageRequest request) {
        if (request.getPage() == null) {
            return ResponseEntity.ok(userService.findAll(request.getStatus()));
        }

        Page<UserDTO> page = userService.findWithSpec(request);

        return ResponseEntity.ok(PageResponse.<UserDTO>builder()
                .page(page.getPageable().getPageNumber())
                .size(page.getPageable().getPageSize())
                .totalElements(page.getTotalElements())
                .data(page.getContent())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@Min(1) @PathVariable Integer id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserRequest dto) {
        return ResponseEntity.ok(userService.insert(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody UserRequest dto) {
        dto.setId(id);
        return ResponseEntity.ok(userService.update(dto));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        userService.delete(ids);
        return ResponseEntity.ok("Delete Successfully");
    }
}
