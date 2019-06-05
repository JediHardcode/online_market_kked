package com.gmail.derynem.service;

import com.gmail.derynem.service.model.item.ItemDTO;

import java.io.InputStream;
import java.util.List;

public interface XmlService {
    boolean isValidXmlFile(InputStream stream);

    List<ItemDTO> parseXml(InputStream stream);
}