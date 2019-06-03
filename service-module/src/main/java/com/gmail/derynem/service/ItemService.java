package com.gmail.derynem.service;

import com.gmail.derynem.service.exception.ItemServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.item.ItemDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ItemService {
    PageDTO<ItemDTO> getItemPageInfo(Integer page, Integer limit);

    void deleteItem(Long id) throws ItemServiceException;

    ItemDTO getItem(Long id) throws ItemServiceException;

    void addItem(ItemDTO item);

    void addItemsFromFile(MultipartFile file) throws ItemServiceException;
}