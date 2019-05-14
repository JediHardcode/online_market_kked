package com.gmail.derynem.service.impl;

import com.gmail.derynem.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.gmail.derynem.repository.constants.DataBaseConstants.OFFSET_LIMIT;

@Component
public class PageServiceImpl implements PageService {
    private final Logger logger = LoggerFactory.getLogger(PageServiceImpl.class);

    @Override
    public int getPages(int countOfObjects) {
        logger.info(" page service get {}", countOfObjects);
        int countOfPages = (countOfObjects + OFFSET_LIMIT - 1) / OFFSET_LIMIT;
        logger.info("count of pages is {} with offset :{}", countOfPages, OFFSET_LIMIT);
        return countOfPages;
    }

    @Override
    public int getOffset(Integer page, int countOfPages) {
        if (page <= 0) {
            page = 1;
        } else if (page > countOfPages && countOfPages != 0) {
            page = countOfPages;
        }
        logger.info("page is {}", page);
        return (page * OFFSET_LIMIT) - OFFSET_LIMIT;
    }
}