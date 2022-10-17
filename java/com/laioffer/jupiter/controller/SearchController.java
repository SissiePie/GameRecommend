package com.laioffer.jupiter.controller;

import com.laioffer.jupiter.entity.db.Item;
import com.laioffer.jupiter.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class SearchController {
    @Autowired
    private GameService gameService;
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    //will auto convert the response entity to Json style
    // here will convert map to Json style response to frontend
    public Map<String, List<Item>> search(@RequestParam(value = "game_id") String gameId) {
        return gameService.searchItems(gameId);
    }
}

/*

send search request  and call searchItems method by passing the  gameId, to get all the type : items
searchItems method will return a map with type and list of corresponding items
by calling the searchByType(String gameId, ItemType type, int limit) method to match the itemType
and trigger the searchStreams/searchVideos/searchClips by passing the gameId and default limit to get the itemList of
certain type; return to the searchItems Method;

searchItems will put the typeName and corresponding itemList to the map and return to the SearchController.




 */
