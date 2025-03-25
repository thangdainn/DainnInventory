package org.dainn.dainninventory.controller;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.notification.Notification;
import org.dainn.dainninventory.service.impl.NotificationService;
import org.dainn.dainninventory.utils.enums.NotificationStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class WebsocketController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getAll() {

        return null;
    }
}
