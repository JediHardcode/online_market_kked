package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.ItemService;
import com.gmail.derynem.service.exception.ItemServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.item.ItemDTO;
import com.gmail.derynem.service.model.order.OrderDTO;
import com.gmail.derynem.service.model.user.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Arrays;

import static com.gmail.derynem.web.constants.PageNamesConstant.*;
import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_LIMIT;
import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_PAGE;
import static com.gmail.derynem.web.constants.PageParamConstant.MESSAGE_PARAM;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_ITEMS_PAGE;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_NOT_FOUND;

@Controller
public class ItemController {
    private final static Logger logger = LoggerFactory.getLogger(ItemController.class);
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/public/items")
    public String showItemsPage(Model model,
                                @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) Integer limit,
                                Authentication authentication,
                                OrderDTO orderDTO) {
        PageDTO<ItemDTO> itemPageInfo = itemService.getItemPageInfo(page, limit);
        itemPageInfo.setLimit(limit);
        itemPageInfo.setPage(page);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        model.addAttribute("user", userPrincipal.getUser());
        model.addAttribute("page", itemPageInfo);
        model.addAttribute("order", orderDTO);
        return ITEMS_PAGE;
    }

    @PostMapping("/private/items/delete")
    public String deleteItem(@RequestParam(value = "id") Long id) {
        try {
            itemService.deleteItem(id);
            return REDIRECT_ITEMS_PAGE + String.format(MESSAGE_PARAM, "item deleted");
        } catch (ItemServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_NOT_FOUND;
        }
    }

    @GetMapping("/public/items/{id}")
    public String showItemsPage(@PathVariable(value = "id") Long id,
                                Model model) {
        try {
            ItemDTO itemDTO = itemService.getItem(id);
            model.addAttribute("item", itemDTO);
            return ITEM_PAGE;
        } catch (ItemServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_NOT_FOUND;
        }
    }

    @GetMapping("/private/items/copy")
    public String showCopyItemPage(@RequestParam(value = "id") Long id,
                                   Model model) {
        try {
            ItemDTO itemDTO = itemService.getItem(id);
            model.addAttribute("item", itemDTO);
            return ITEM_COPY_PAGE;
        } catch (ItemServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_NOT_FOUND;
        }
    }

    @PostMapping("/private/items/copy")
    public String copyItem(@ModelAttribute(value = "item") @Valid ItemDTO item,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("item not valid, errors {}", Arrays.toString(bindingResult.getAllErrors().toArray()));
            return ITEM_COPY_PAGE;
        }
        itemService.addItem(item);
        return REDIRECT_ITEMS_PAGE + String.format(MESSAGE_PARAM, "item copied");
    }

    @PostMapping("/private/items/upload")
    public String uploadItem(@RequestParam("file") MultipartFile multipartFile) {
        try {
            itemService.addItemsFromFile(multipartFile);
            return REDIRECT_ITEMS_PAGE + String.format(MESSAGE_PARAM, "items added");
        } catch (ItemServiceException e) {
            logger.error(e.getMessage(), e);
            return CUSTOM_ERROR;
        }
    }

    @GetMapping("/private/items/upload")
    public String showUploadFilePage() {
        return UPLOAD_ITEMS_PAGE;
    }
}