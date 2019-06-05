package com.gmail.derynem.service.validation.constraint;

import com.gmail.derynem.service.XmlService;
import com.gmail.derynem.service.validation.annotation.ValidFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;

public class ValidFileValidator implements ConstraintValidator<ValidFile, MultipartFile> {
    private final static Logger logger = LoggerFactory.getLogger(ValidFileValidator.class);
    private final XmlService xmlService;

    public ValidFileValidator(XmlService xmlService) {
        this.xmlService = xmlService;
    }

    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file.getSize() == 0) {
            logger.info(" file is empty");
            return false;
        } else {
            try (InputStream stream = file.getInputStream()) {
                return xmlService.isValidXmlFile(stream);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return false;
            }
        }
    }
}