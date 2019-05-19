package com.gmail.derynem.service.model;

import java.util.ArrayList;
import java.util.List;

public class PageDTO<T> {
    private List<T> entities = new ArrayList<>();
    private int countOfPages;
    private int limit;
    private int page;

    public PageDTO() {
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }

    public int getCountOfPages() {
        return countOfPages;
    }

    public void setCountOfPages(int countOfPages) {
        this.countOfPages = countOfPages;
    }
}