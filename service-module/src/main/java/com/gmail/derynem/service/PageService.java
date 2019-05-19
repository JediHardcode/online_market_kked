package com.gmail.derynem.service;

public interface PageService {
    int getPages(int countOfObjects, int limit);

    int getOffset(Integer page, int countOfPages, int limit);
}