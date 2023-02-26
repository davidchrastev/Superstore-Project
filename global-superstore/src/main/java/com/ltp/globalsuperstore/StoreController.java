package com.ltp.globalsuperstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
public class StoreController {

    StoreService storeService = new StoreService();



    @GetMapping("/")
    public String getForm(Model model, @RequestParam(required = false) String id) {
        int index = storeService.getItemIndex(id);
        Item item;

        if (index == Constants.NOT_FOUND) {
            item = new Item();
        } else {
            item = storeService.getItem(index);
        }

        model.addAttribute("item", item);



        return "form";
    }

    @PostMapping("/submitItem")
    public String handleSubmit(@Valid Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (item.getPrice() < item.getDiscount()) {
            bindingResult.rejectValue("price", "", "Price cannot be less than discount");
        }

        int index = storeService.getItemIndex(item.getId());
        String status = Constants.SUCCESS_STATUS;

        if (bindingResult.hasErrors()) {
            return "form";
        }
        if (index == Constants.NOT_FOUND) {
            storeService.addItem(item);
        } else if (storeService.within5Days(item.getDate(), storeService.getItem(index).getDate())){
            storeService.updateItem(item, index);
        } else {
            status = Constants.FAILED_STATUS;
        }
        redirectAttributes.addFlashAttribute("status", status);

        return "redirect:/inventory";
    }

    @GetMapping("/inventory")
    public String getInventory(Model model) {
        model.addAttribute("items", storeService.getItems());
        return "inventory";
    }
}
