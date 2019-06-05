package com.gmail.derynem.service.impl;

import com.gmail.derynem.service.XmlService;
import com.gmail.derynem.service.model.item.Catalog;
import com.gmail.derynem.service.model.item.ItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Component
public class XmlServiceImpl implements XmlService {
    private final static Logger logger = LoggerFactory.getLogger(XmlServiceImpl.class);

    @Override
    public boolean isValidXmlFile(InputStream stream) {
        File schemaFile = new File(this.getClass().getResource("/static/xsd/scheme.xsd").getFile());
        Source xml = new StreamSource(stream);
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            validator.validate(xml);
            logger.info("file valid!");
            return true;
        } catch (Exception e) {
            logger.error("file not valid: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<ItemDTO> parseXml(InputStream stream) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Catalog.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Catalog catalog = (Catalog) unmarshaller.unmarshal(stream);
            logger.info(" parse successfully finished, count of items {}", catalog.getListOfItems().size());
            return catalog.getListOfItems();
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}