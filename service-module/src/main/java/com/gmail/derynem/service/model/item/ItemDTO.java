package com.gmail.derynem.service.model.item;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.gmail.derynem.service.validation.constant.ValidationConstant.ENG_LETTER_WITH_DIGITS;
import static com.gmail.derynem.service.validation.constant.ValidationConstant.ITEM_DESCRIPTION_LENGTH;
import static com.gmail.derynem.service.validation.constant.ValidationConstant.PRICE_PATTERN;

@XmlRootElement(name = "item")
public class ItemDTO {
    private Long id;
    @NotNull
    @NotEmpty
    @Pattern(regexp = ENG_LETTER_WITH_DIGITS, message = "{item.name.not.valid}")
    private String name;
    private boolean isDeleted;
    private String uniqueCode;
    @NotNull
    @NotEmpty
    @Size(max = ITEM_DESCRIPTION_LENGTH)
    private String description;
    @NotNull
    @NotEmpty
    @Pattern(regexp = PRICE_PATTERN, message = "{item.price.not.valid}")
    private String price;

    public ItemDTO() {
        isDeleted = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    @XmlElement(name = "price")
    public void setPrice(String price) {
        this.price = price;
    }
}