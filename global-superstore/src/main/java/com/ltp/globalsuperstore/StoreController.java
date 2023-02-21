package com.ltp.globalsuperstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StoreController {

    List<Item> items = new ArrayList<>();

    @GetMapping("/")
    public String getForm(Model model, @RequestParam(required = false) String id) {
        int index = getItemIndex(id);
        Item item;

        if (index == Constants.NOT_FOUND) {
            item = new Item();
        } else {
            item = items.get(index);
        }

        model.addAttribute("item", item);
        model.addAttribute("categories", Constants.CATEGORIES);
        return "form";
    }

    @PostMapping("/submitItem")
    public String handleSubmit(Item item) {
        int index = getItemIndex(item.getId());

        if (index == Constants.NOT_FOUND) {
            items.add(item);
        } else {
            items.set(index, item);
        }

        return "redirect:/inventory";
    }

    @GetMapping("/inventory")
    public String getInventory(Model model) {
        model.addAttribute("items", items);
        return "inventory";
    }


    public Integer getItemIndex(String id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(id)) {
                return i;
            }
        }
        return Constants.NOT_FOUND;
    }

}
