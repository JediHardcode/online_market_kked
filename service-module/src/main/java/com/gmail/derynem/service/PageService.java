package com.gmail.derynem.service;

public interface PageService {
    int getPages(int countOfObjects);

    int getValidPage(Integer page, int countOfPages);

    int getOffset(int page);
}
