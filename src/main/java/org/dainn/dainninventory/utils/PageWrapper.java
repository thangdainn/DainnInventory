package org.dainn.dainninventory.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageWrapper<T> {
    private List<T> list;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;

}
