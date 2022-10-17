package com.laioffer.jupiter.controller;

import com.laioffer.jupiter.dao.FavoriteDao;
import com.laioffer.jupiter.entity.Request.FavoriteRequestBody;
import com.laioffer.jupiter.entity.db.Item;
import com.laioffer.jupiter.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FavoriteController {
    // three methods defined in favorite API;
    // setFavoriteItem, getFavoriteItem and unsetFavoriteItem.
    @Autowired
    private FavoriteService favoriteService;

    // define url
    @RequestMapping(value = "/favorite", method = RequestMethod.POST)
    // @RequestBody is deserilize the JasonString into FavoriteRequestBody class,
    // we will map the Item class filed in favoriteRequestBody class;
    // HttpServletRequest request is the request with the sessionId
    public void setFavoriteItem(@RequestBody FavoriteRequestBody requestBody,
                                HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(false);
        // set a false bc we don't need to create a new session, if we didn't assign false, it will create a new session.
        // if we can't find the corresponding session of current request
        if (session == null){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            // tell the client need to login first
            return;
        }
        String userId = (String)session.getAttribute("user_id");
        favoriteService.setFavoriteItem(userId, requestBody.getItem());
    }

    @RequestMapping(value = "/favorite", method = RequestMethod.DELETE)
    public void unsetFavoriteItem(@RequestBody FavoriteRequestBody requestBody, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String userId = (String) session.getAttribute("user_id");
        favoriteService.unsetFavoriteItem(userId, requestBody.getItem().getId());
    }


    @RequestMapping(value = "/favorite", method = RequestMethod.GET)
    @ResponseBody
    // bc we need to return json
    public Map<String, List<Item>> getFavoriteItems(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(false);
        if(session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return new HashMap<>();
        }
        String userId = (String) session.getAttribute("user_id");
        return favoriteService.getFavoriteItems(userId);
    }


}

/*
   set/unset/get favorite  item
   before we implement these operations, we need to check the sessionId, to make sure the user has logged in;
   FavoriteService dependency will inject into the controller class;
  set---> post http method
    call setFavoriteItem() in favoriteService class with passing the parameters (that deserilize by @RequestBody)
      and mapped to Item Class to get the Item Object), and request with session ----->
            favoriteService will call setFavoriteItem in FavoriteDao --->
                setFavoriteItem






 */
