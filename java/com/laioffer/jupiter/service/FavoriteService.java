package com.laioffer.jupiter.service;

import com.laioffer.jupiter.dao.FavoriteDao;
import com.laioffer.jupiter.entity.db.Item;
import com.laioffer.jupiter.entity.db.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import java.util.*;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteDao favoriteDao;

    public void setFavoriteItem (String userId, Item item){
        favoriteDao.setFavoriteItem(userId, item);
    }

    public void unsetFavoriteItem(String userId, String itemId){
        favoriteDao.unsetFavoriteItem(userId, itemId);
    }

    public Map<String, List<Item>> getFavoriteItems(String userId){
        // classify the favorite items by different type;
        Map<String, List<Item>> itemMap = new HashMap<>();
        // put the different type as the key into the map ; then add corresponding items as the value;
        for(ItemType type : ItemType.values()){
            itemMap.put(type.toString(), new ArrayList<>());
        }
        //get all the favorite items of this user by checking his userId;
        Set<Item> favorites = favoriteDao.getFavoriteItems(userId);
        //  get the corresponding key by get ItemType of the favorite item,
        //  then add the item to the list as the value;
        for(Item item : favorites){
            itemMap.get(item.getItemType().toString()).add(item);
        }
        return itemMap;
    }
}
