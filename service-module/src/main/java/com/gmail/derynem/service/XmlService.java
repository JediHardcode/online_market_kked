package com.gmail.derynem.service;

import com.gmail.derynem.service.model.item.ItemDTO;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface XmlService {
    boolean isValidXmlFile(Path file);

    List<ItemDTO> parseXml(File file);
}