package com.gmail.derynem.web.api;

import com.gmail.derynem.service.ItemService;
import com.gmail.derynem.service.exception.ItemServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.item.ItemDTO;
import com.gmail.derynem.web.controller.ItemController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;

import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_LIMIT;
import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_PAGE;

@RestController
@RequestMapping("/api/v1")
public class ApiItemController {
    private final static Logger logger = LoggerFactory.getLogger(ItemController.class);
    private final ItemService itemService;

    public ApiItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public ResponseEntity<PageDTO<ItemDTO>> getListOfItems(
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) Integer limit) {
        PageDTO<ItemDTO> itemPageInfo = itemService.getItemPageInfo(page, limit);
        return new ResponseEntity<>(itemPageInfo, HttpStatus.OK);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<ItemDTO> getItem(@PathVariable(value = "id") Long id) {
        try {
            ItemDTO itemDTO = itemService.getItem(id);
            return new ResponseEntity<>(itemDTO, HttpStatus.OK);
        } catch (ItemServiceException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/items")
    public ResponseEntity addItem(@RequestBody @Valid ItemDTO item,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("item not valid, errors :{}", Arrays.toString(bindingResult.getAllErrors().toArray()));
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        itemService.addItem(item);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity deleteItem(@PathVariable(value = "id") Long id) {
        try {
            itemService.deleteItem(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ItemServiceException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}