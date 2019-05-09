package com.gmail.derynem.service.impl;

import com.gmail.derynem.service.PageService;
import com.gmail.derynem.service.model.PageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PageServiceImpl implements PageService {
    private final Logger logger = LoggerFactory.getLogger(PageServiceImpl.class);

    @Override
    public PageDTO getPages(int countOfPages) {
        logger.info(" page service get {}", countOfPages);
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
}
