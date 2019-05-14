package com.gmail.derynem.service;

public interface PageService {
    int getPages(int countOfObjects);

    int getOffset(Integer page, int countOfPages);
}