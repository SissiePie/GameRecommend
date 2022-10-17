package com.laioffer.jupiter.controller;

import com.laioffer.jupiter.entity.db.Item;
import com.laioffer.jupiter.service.RecommendationException;
import com.laioffer.jupiter.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;
    @RequestMapping(value = "/recommendation", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, List<Item>> recommendation(HttpServletRequest request) throws ServletException {
        HttpSession session = request.getSession(false);
        // false means not requirement.
        Map<String, List<Item>> itemMap;
        try {
            if (session == null) {
                // if is null then the user doesn't log in then just return the default recommendation
                itemMap = recommendationService.recommendItemsByDefault();
            } else {
                // if we got the session of the user, then return the recommendation by userId;
                String userId = (String) session.getAttribute("user_id");
                itemMap = recommendationService.recommendItemsByUser(userId);
            }
        } catch (RecommendationException e){
            throw new ServletException(e);
        }
        return itemMap;
    }
}


/*
once we receive the recommendation request, we will check the session first(to check if the user login)
if the user is not login before, we will do the default;
otherwise we will do the recommendation by userId;

default will return all the type items of the topGame list; recommend by topGame;

Given a user, get all the items (ids) this user has favorited.
Set<String> itemIds = dao.getFavoriteItemIds(userId);
Given all these items, get their gameIds and sort by count.
Map<String, List<String>> keywords = dao.getFavoriteGameIds(itemIds);
Given these gameIds, use Twitch API with , then filter out user favorited items.
List<Item> items = gameService.searchByType(gameId, type);



 */
