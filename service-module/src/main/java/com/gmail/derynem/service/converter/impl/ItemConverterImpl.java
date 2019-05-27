package com.gmail.derynem.service.converter.impl;

import com.gmail.derynem.repository.model.Item;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.model.item.ItemDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Qualifier("itemConverter")
public class ItemConverterImpl implements Converter<ItemDTO, Item> {
    @Override
    public ItemDTO toDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setDeleted(item.isDeleted());
        itemDTO.setDescription(item.getDescription());
        itemDTO.setId(item.getId());
        itemDTO.setName(item.getName());
        itemDTO.setUniqueCode(item.getUniqueCode());
        itemDTO.setPrice(String.valueOf(item.getPrice()));
        return itemDTO;
    }

    @Override
    public Item toEntity(ItemDTO itemDTO) {
        Item item = new Item();
        item.setDeleted(itemDTO.isDeleted());
        item.setDescription(itemDTO.getDescription());
        item.setName(itemDTO.getName());
        item.setPrice(new BigDecimal(itemDTO.getPrice()));
        item.setUniqueCode(itemDTO.getUniqueCode());
        return item;
    }
}