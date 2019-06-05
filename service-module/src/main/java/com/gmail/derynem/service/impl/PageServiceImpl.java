package com.gmail.derynem.service.impl;

import com.gmail.derynem.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.gmail.derynem.service.model.enums.LimitEnum.LIMIT_THIRD;

@Component
public class PageServiceImpl implements PageService {
    private final Logger logger = LoggerFactory.getLogger(PageServiceImpl.class);

    @Override
    public int getPages(int countOfObjects, Integer limit) {
        logger.info(" page service get {}", countOfObjects);
        int countOfPages = (countOfObjects + limit - 1) / limit;
        logger.info("count of pages is {} with offset :{}", countOfPages, limit);
        return countOfPages;
    }

    @Override
    public int getOffset(Integer page, int countOfPages, Integer limit) {
        if (page <= 0) {
            page = 1;
        } else if (page > countOfPages && countOfPages != 0) {
            page = countOfPages;
        }
        logger.info("page is {}", page);
        return (page * limit) - limit;
    }

    @Override
    public int validateLimit(Integer limit) {
        if (limit <= 0) {
            limit = 1;
        }
        if (limit > LIMIT_THIRD.getLimit()) {
            limit = LIMIT_THIRD.getLimit();
        }
        return limit;
    }
}