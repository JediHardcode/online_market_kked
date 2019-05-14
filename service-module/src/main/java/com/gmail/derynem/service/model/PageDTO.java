package com.gmail.derynem.service.model;

import java.util.ArrayList;
import java.util.List;

public class PageDTO<T> {
    private List<T> objects = new ArrayList<>();
    private int countOfPages;

    public PageDTO() {
    }

    public List<T> getObjects() {
        return objects;
    }

    public void setObjects(List<T> objects) {
        this.objects = objects;
    }

    public int getCountOfPages() {
        return countOfPages;
    }

    public void setCountOfPages(int countOfPages) {
        this.countOfPages = countOfPages;
    }
}