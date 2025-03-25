package org.dainn.dainninventory.dto.notification;

import lombok.*;
import org.dainn.dainninventory.utils.enums.NotificationStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Notification {
    private NotificationStatus status;
    private String message;
    private String title;
}
