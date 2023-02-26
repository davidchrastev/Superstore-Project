package com.ltp.globalsuperstore;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StoreService {


    StoreRepository storeRepository = new StoreRepository();

    public Item getItem(int index) {
        return storeRepository.getItem(index);
    }
    public void addItem(Item item) {
        storeRepository.addItem(item);
    }
    public void updateItem(Item item, int index) {
        storeRepository.updateItem(item, index);
    }
    public List<Item> getItems() {
      return storeRepository.getItems();
    }

    public Integer getItemIndex(String id) {
        for (int i = 0; i < getItems().size(); i++) {
            if (getItem(i).getId().equals(id)) {
                return i;
            }
        }
        return Constants.NOT_FOUND;
    }

    public boolean within5Days(Date newDate, Date oldDate) {
        long diff = Math.abs(newDate.getTime() - oldDate.getTime());
        return (int) (TimeUnit.MILLISECONDS.toDays(diff)) <= 5;
    }

    public String handleSubmit(Item item) {
        int index = getItemIndex(item.getId());
        String status = Constants.SUCCESS_STATUS;

        if (index == Constants.NOT_FOUND) {
            addItem(item);
        } else if (within5Days(item.getDate(), getItem(index).getDate())){
            updateItem(item, index);
        } else {
            status = Constants.FAILED_STATUS;
        }

        return status;
    }
    public Item getItemById(String id) {
        Item item;
        int index = getItemIndex(id);
        if (index == Constants.NOT_FOUND) {
            item = new Item();
        } else {
            item = getItem(index);
        }
        return item;
    }
}
