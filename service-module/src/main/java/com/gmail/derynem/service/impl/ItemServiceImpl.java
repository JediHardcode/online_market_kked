package com.gmail.derynem.service.impl;

import com.gmail.derynem.repository.ItemRepository;
import com.gmail.derynem.repository.model.Item;
import com.gmail.derynem.service.ItemService;
import com.gmail.derynem.service.PageService;
import com.gmail.derynem.service.RandomService;
import com.gmail.derynem.service.XmlService;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.exception.ItemServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.item.ItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final static Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
    private final ItemRepository itemRepository;
    private final PageService pageService;
    private final Converter<ItemDTO, Item> converter;
    private final RandomService randomService;
    private final XmlService xmlService;

    public ItemServiceImpl(ItemRepository itemRepository,
                           PageService pageService,
                           @Qualifier("itemConverter") Converter<ItemDTO, Item> converter,
                           RandomService randomService,
                           XmlService xmlService) {
        this.itemRepository = itemRepository;
        this.pageService = pageService;
        this.converter = converter;
        this.randomService = randomService;
        this.xmlService = xmlService;
    }

    @Override
    @Transactional
    public PageDTO<ItemDTO> getItemPageInfo(Integer page, Integer limit) {
        int countOfItems = itemRepository.getCountOfEntities();
        int countOfPages = pageService.getPages(countOfItems, limit);
        int offset = pageService.getOffset(page, countOfPages, limit);
        PageDTO<ItemDTO> itemPageInfo = new PageDTO<>();
        itemPageInfo.setCountOfPages(countOfPages);
        List<Item> items = itemRepository.findAll(offset, limit);
        List<ItemDTO> articleDTOS = items.stream()
                .map(converter::toDTO)
                .collect(Collectors.toList());
        itemPageInfo.setEntities(articleDTOS);
        logger.info("count of items {}, count of pages {}",
                itemPageInfo.getEntities().size(), itemPageInfo.getCountOfPages());
        itemPageInfo.setLimit(limit);
        itemPageInfo.setPage(page);
        return itemPageInfo;
    }

    @Override
    @Transactional
    public void deleteItem(Long id) throws ItemServiceException {
        Item item = itemRepository.getById(id);
        if (item == null) {
            throw new ItemServiceException("item with id " + id + " doest exist in database");
        } else {
            itemRepository.remove(item);
            logger.info(" item with id {} and name {} deleted ", item.getId(), item.getName());
        }
    }

    @Override
    @Transactional
    public ItemDTO getItem(Long id) throws ItemServiceException {
        Item item = itemRepository.getById(id);
        if (item == null) {
            throw new ItemServiceException("item with id  " + id + " +  doest exist in database");
        } else {
            ItemDTO itemDTO = converter.toDTO(item);
            logger.info(" item found! item name {}", itemDTO.getName());
            return itemDTO;
        }
    }

    @Override
    @Transactional
    public void addItem(ItemDTO item) {
        item.setUniqueCode(randomService.generateUniqueNum());
        Item newItem = converter.toEntity(item);
        itemRepository.persist(newItem);
        logger.info("item added, item name is {}", newItem.getName());
    }

    @Override
    @Transactional
    public void addItemsFromFile(MultipartFile file) throws ItemServiceException {
        try (InputStream stream = file.getInputStream()) {
            List<ItemDTO> items = xmlService.parseXml(stream);
            items.stream()
                    .peek(itemDTO -> itemDTO.setUniqueCode(randomService.generateUniqueNum()))
                    .map(converter::toEntity)
                    .forEach(itemRepository::persist);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ItemServiceException(e.getMessage(), e);
        }
    }
}