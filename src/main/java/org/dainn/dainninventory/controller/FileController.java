package org.dainn.dainninventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.config.endpoint.Endpoint;
import org.dainn.dainninventory.dto.response.CloudinaryResponse;
import org.dainn.dainninventory.service.IImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(Endpoint.File.BASE)
@RequiredArgsConstructor
public class FileController {
    private final IImageService imageService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestPart("file") MultipartFile file) {
        CloudinaryResponse response = CloudinaryResponse.builder()
                .url(imageService.uploadImage(file))
                .build();
        return ResponseEntity.ok(response);
    }
}