package org.dainn.dainninventory.controller.response;

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
    private Integer totalPages;
    private List<T> data = new ArrayList<>();
}
