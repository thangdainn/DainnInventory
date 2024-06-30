package org.dainn.dainninventory.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dainn.dainninventory.utils.constant.PageConstant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class PageRequest {
    private Integer page;
    private Integer size = PageConstant.DEFAULT_PAGE_SIZE;
    private String sortBy = PageConstant.DEFAULT_SORT_BY;
    private String sortDir = PageConstant.DEFAULT_SORT_DIRECTION;
}
