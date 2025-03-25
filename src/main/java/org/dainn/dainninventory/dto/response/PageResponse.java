package org.dainn.dainninventory.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    private Integer page;
    private Integer size;
    private long totalElements;
    private List<T> data = new ArrayList<>();
}
