package com.gmail.derynem.service.model.enums;

public enum LimitEnum {
    LIMIT_FIRST(5), LIMIT_SECOND(10), LIMIT_TRIRD(15);

    private int limit;

    LimitEnum(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}