package com.gmail.derynem.service;

import com.gmail.derynem.service.model.PageDTO;

public interface PageService {
    PageDTO getPages(int countOfObjects);

    int getValidPage(Integer page, int countOfPages);

    int getOffset(int page);
}
