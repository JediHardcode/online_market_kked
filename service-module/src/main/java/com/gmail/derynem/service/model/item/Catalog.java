package com.gmail.derynem.service.model.item;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Catalog")
public class Catalog {
    private List<ItemDTO> listOfItems;

    @XmlElement(name = "item")
    public void setListOfItems(List<ItemDTO> listOfItems) {
        this.listOfItems = listOfItems;
    }

    public List<ItemDTO> getListOfItems() {
        return listOfItems;
    }
}