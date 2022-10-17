package com.laioffer.jupiter.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.laioffer.jupiter.entity.db.Item;
import com.laioffer.jupiter.entity.db.ItemType;
import com.laioffer.jupiter.entity.db.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FavoriteDao {
    //dao ---> data access object, interact with data;
    //data access object
    @Autowired
    private SessionFactory sessionFactory;
    // insert a favorite record to the database
    public void setFavoriteItem(String userId, Item item){
        Session session = null;

       try{
           session = sessionFactory.openSession();
           // get corresponding user in User class  and find the correct userId;
           User user = session.get(User.class, userId);
           // if the item is already stored in the itemSet, it won't add duplicate;
           user.getItemSet().add(item);
           // insert item into item table,
           // at the same time itemId group with userId insert into the
           // but only itemId and userId, will store in the favorite table;
           session.beginTransaction();
           session.save(user);
           session.getTransaction().commit();
       }catch (Exception ex){
           ex.printStackTrace();
           session.getTransaction().rollback();
       }finally {
           if(session != null) session.close();
       }
    }


    public void unsetFavoriteItem(String userId, String itemId){
        // remove a favorite record from the database
        // we can't delete the item from the database;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            User user = session.get(User.class, userId);
            Item item = session.get(Item.class, itemId);
            // here is removing the unfavorite item from the user's favorite item set;
            user.getItemSet().remove(item);
            session.beginTransaction();
            // update users favorite item set;
            session.update(user);
            session.getTransaction().commit();
        } catch(Exception ex){
            ex.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if(session != null){
                session.close();
            }
        }
    }

    public Set<Item> getFavoriteItems(String userId){
        try (Session session = sessionFactory.openSession()){
            return session.get(User.class, userId).getItemSet();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        // if the set is empty, then just return a empty set;
        return new HashSet<>();
    }

    // get all favorite item(ids) for the given users
    public Set<String> getFavoriteItemIds(String userId){
       Set<String> itemIds = new HashSet<>();
       Session session = null;
       try {
          session = sessionFactory.openSession();
          Set<Item> items = session.get(User.class, userId).getItemSet();
          for(Item item : items){
              itemIds.add(item.getId());
          }
       } catch(Exception ex){
            ex.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if(session != null){
                session.close();
            }
        }
       return itemIds;
    }

    // Get favorite items for the given user.
    // The returned map includes three entries like {"Video": [item1, item2, item3],
    // "Stream": [item4, item5, item6], "Clip": [item7, item8, ...]}
    public Map<String, List<String>> getFavoriteGameIds(Set<String> favoriteItemsIds) {
        Map<String, List<String>> itemMap = new HashMap<>();
        for(ItemType type : ItemType.values()){
            itemMap.put(type.toString(), new ArrayList<>());
        }
        try(Session session = sessionFactory.openSession()){
            for(String itemId : favoriteItemsIds) {
                Item item = session.get(Item.class, itemId);

                itemMap.get(item.getItemType().toString()).add(item.getGameId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return itemMap;
    }

    /*
            public static void main(String[ ] args){
                SearchService test = new SearchService();
                test.searchGame("game_ name");
            }



     */

}
