package org.dainn.dainninventory.service;

import org.dainn.dainninventory.controller.request.ProductPageRequest;
import org.dainn.dainninventory.dto.InventoryDTO;
import org.dainn.dainninventory.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IInventoryService {
    InventoryDTO save(InventoryDTO dto);
}
