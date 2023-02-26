package com.ltp.globalsuperstore;

import java.util.ArrayList;
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
        for (int i = 0; i < storeRepository.getItems().size(); i++) {
            if (storeRepository.getItem(i).getId().equals(id)) {
                return i;
            }
        }
        return Constants.NOT_FOUND;
    }

    public boolean within5Days(Date newDate, Date oldDate) {
        long diff = Math.abs(newDate.getTime() - oldDate.getTime());
        return (int) (TimeUnit.MILLISECONDS.toDays(diff)) <= 5;
    }
}
