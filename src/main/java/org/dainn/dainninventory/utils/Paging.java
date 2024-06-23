package org.dainn.dainninventory.utils;

import org.dainn.dainninventory.controller.request.UserPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Paging {
    public static Pageable getPageable(org.dainn.dainninventory.controller.request.PageRequest request) {
        Sort sort;
        if (request.getSortBy() != null && !request.getSortBy().isBlank()) {
            sort = request.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(request.getSortBy()).ascending() : Sort.by(request.getSortBy()).descending();
        } else {
            sort = Sort.unsorted();
        }
        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }
}
