package com.gmail.derynem.service.impl;

import com.gmail.derynem.service.PageService;
import com.gmail.derynem.service.model.PageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.gmail.derynem.repository.constants.DataBaseConstants.OFFSET_LIMIT;

@Component
public class PageServiceImpl implements PageService {
    private final Logger logger = LoggerFactory.getLogger(PageServiceImpl.class);

    @Override
    public PageDTO getPages(int countOfObjects) {
        int countOfPages = (countOfObjects + OFFSET_LIMIT - 1) / OFFSET_LIMIT;
        logger.info(" page service get {}", countOfObjects);
        PageDTO pageDTO = new PageDTO();
        for (int i = 0; i < countOfPages; i++) {
            pageDTO.getCount().add(i + 1);
        }
        logger.info("page service  return page dto with list size {}", pageDTO.getCount().size());
        return pageDTO;
    }

    @Override
    public int getValidPage(Integer page, int countOfPages) {
        if (page == null) {
            page = 1;
        } else if (page > countOfPages) {
            page = countOfPages;
        }
        logger.info("page is {}", page);
        return page;
    }

    @Override
    public int getOffset(int page) {
        return (page * OFFSET_LIMIT) - OFFSET_LIMIT;
    }
}
