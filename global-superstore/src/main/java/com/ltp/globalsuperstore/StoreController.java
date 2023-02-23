package com.ltp.globalsuperstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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



        return "form";
    }

    @PostMapping("/submitItem")
    public String handleSubmit(@Valid Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (item.getPrice() < item.getDiscount()) {
            bindingResult.rejectValue("price", "", "Price cannot be less than discount");
        }

        int index = getItemIndex(item.getId());
        String status = Constants.SUCCESS_STATUS;

        if (bindingResult.hasErrors()) {
            return "form";
        }
        if (index == Constants.NOT_FOUND) {
            items.add(item);
        } else if (within5Days(item.getDate(), items.get(index).getDate())){
            items.set(index, item);
        } else {
            status = Constants.FAILED_STATUS;
        }
        redirectAttributes.addFlashAttribute("status", status);

        return "redirect:/inventory";
    }

    @GetMapping("/inventory")
    public String getInventory(Model model) {
        model.addAttribute("items", items);
        return "inventory";
    }

    public boolean within5Days(Date newDate, Date oldDate) {
        long diff = Math.abs(newDate.getTime() - oldDate.getTime());
        return (int) (TimeUnit.MILLISECONDS.toDays(diff)) <= 5;
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
