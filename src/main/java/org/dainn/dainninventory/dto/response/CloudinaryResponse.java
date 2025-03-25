package org.dainn.dainninventory.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CloudinaryResponse {
    private String url;
    private boolean success = true;
}
