package com.gmail.derynem.service;

public interface PageService {
    int getPages(int countOfObjects, Integer limit);

    int getOffset(Integer page, int countOfPages, Integer limit);

    int validateLimit(Integer limit);
}