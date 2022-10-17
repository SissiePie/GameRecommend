package com.laioffer.jupiter.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.jupiter.entity.db.Item;
import com.laioffer.jupiter.entity.db.ItemType;
import com.laioffer.jupiter.entity.response.Game;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Service
//同理 service里面已经有了@component的功能
public class GameService {
    //Define five static Strings in GameService clas
    private static final String TOKEN = "Bearer 28s0x6pyamupxhcyxd3x6n7vbjbr95";
    private static final String CLIENT_ID = "synd6jbagj9k3vrm9gdmy0fk0bqmqe";
    private static final String TOP_GAME_URL = "https://api.twitch.tv/helix/games/top?first=%s";
    private static final String GAME_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/games?name=%s";
    private static final int DEFAULT_GAME_LIMIT = 20;
    private static final String STREAM_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/streams?game_id=%s&first=%s";
    private static final String VIDEO_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/videos?game_id=%s&first=%s";
    private static final String CLIP_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/clips?game_id=%s&first=%s";
    private static final String TWITCH_BASE_URL = "https://www.twitch.tv/";
    // add the broadcasterName then we can reach the broadcast room.
    private static final int DEFAULT_SEARCH_LIMIT = 20;



    //Implement buildGameURL function.
    // It will help generate the correct URL when you call Twitch Game API.
    // Build the request URL which will be used when calling Twitch APIs,
    // e.g. https://api.twitch.tv/helix/games/top when trying to get top games.
    private String buildGameURL(String url, String gameName, int limit){
        if (gameName.equals("")){
            return String.format(url,limit);
        } else {
            try{
                // Encode special characters in URL, e.g. Rick Sun -> Rick%20Sun
                //如果client 发来的请求，游戏name 有空格，所以就要做一个encoding
                gameName = URLEncoder.encode(gameName, "UTF-8");
                //encode throws UnsupportedEncodingException
            } catch(UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return String.format(url, gameName);
    }


    // Send HTTP request to Twitch Backend based on the given URL,
    // and returns the body of the HTTP response returned from Twitch backend.
    private String searchTwitch(String url) throws TwitchException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // Define the response handler to parse and return HTTP response body returned from Twitch
        ResponseHandler<String> responseHandler = response -> {
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != 200) {
                // check if we get result success
                System.out.println("Response status: " + response.getStatusLine().getReasonPhrase());
                throw new TwitchException("Failed to get result from Twitch API");
            }
            HttpEntity entity = response.getEntity();
            //HttpEntity is the response data;
            // through response to get the real data
            // firstly check if the result is null
            if (entity == null) {
                throw new TwitchException("Failed to get result from Twitch API");
            }
            // if it's not null return to jsonObject as string
            JSONObject obj = new JSONObject(EntityUtils.toString(entity));
            return obj.getJSONArray("data").toString();
            // only get the key = data, skip other useless result;
        };

        try {
            // Define the HTTP request, TOKEN and CLIENT_ID are
            // used for user authentication on Twitch backend
            HttpGet request = new HttpGet(url);
            request.setHeader("Authorization", TOKEN);
            request.setHeader("Client-Id", CLIENT_ID);
            // send request, through the execute method to get the data from
            //return obj.getJSONArray("data").toString();
            return httpclient.execute(request, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
            throw new TwitchException("Failed to get result from Twitch API");
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Convert JSON format data returned from Twitch to an Arraylist of Game objects
    // after call twitch search we get string data from jasonObject
    //  transfer data to Array by using the readValue method from ObjectMapper class
    private List<Game> getGameList(String data) throws TwitchException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // transfer string to arrayList;
            return Arrays.asList(mapper.readValue(data, Game[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new TwitchException("Failed to parse game data from Twitch API");
        }
    }

    // Integrate search() and getGameList() together, returns the top x popular games from Twitch.
    public List<Game> topGames(int limit) throws TwitchException {
        if (limit <= 0) {
            limit = DEFAULT_GAME_LIMIT;
        }
        return getGameList(searchTwitch(buildGameURL(TOP_GAME_URL, "", limit)));
        //buildGameURL  top game url
        // searchTwitch to get data
        // use getGameList to transfer the jason data to List
    }

    // Integrate search() and getGameList() together, returns the dedicated game based on the game name.
    public Game searchGame(String gameName) throws TwitchException {
        //url = buildGameUrl(GAME_SEARCH_URL_TEMPLATE, gameName, limit )
        // data = searchTwitch(url)
        // gameList = getGameList(data)
        List<Game> gameList = getGameList(searchTwitch(buildGameURL(GAME_SEARCH_URL_TEMPLATE, gameName, 0)));
        if (gameList.size() != 0) {
            return gameList.get(0);
        }
        return null;
    }
    // search a specific game to get its gameId
    // then search by gameId we get to get the  clip/video/stream about this game.
    // then the client can choose what type you want to check.
    // so we also need to implement the clip / video / stream search


    private String buildSearchURL(String url, String gameId, int limit){
        try {
            gameId = URLEncoder.encode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return String.format(url,gameId, limit);
    }

//convert the json data returned from Twitch to a list of Item objects.

    public List<Item> getItemList(String data) throws TwitchException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Arrays.asList(mapper.readValue(data, Item[].class));
        } catch (JsonProcessingException e){
            e.printStackTrace();
            throw new TwitchException("Failed to parse item data from Twitch API");
        }
    }

    // return the top x streams based on gameID
    public List<Item> searchStreams(String gameId, int limit) throws TwitchException{
        List<Item> streams = getItemList(searchTwitch(buildSearchURL(STREAM_SEARCH_URL_TEMPLATE, gameId, limit)));
        for(Item item : streams){
            item.setItemType(ItemType.STREAM);
            item.setUrl(TWITCH_BASE_URL + item.getBroadcasterName());
        }
        return streams;
    }

    public List<Item> searchClips(String gameId, int limit) throws TwitchException{
        String url = buildSearchURL(CLIP_SEARCH_URL_TEMPLATE, gameId, limit);
        String data = searchTwitch(url);
        List<Item> clips = getItemList(data);
        for(Item item : clips){
            item.setItemType(ItemType.CLIP);
        }
        return clips;
    }

    public List<Item> searchVideos(String gameId, int limit) throws TwitchException{
        String url = buildSearchURL(VIDEO_SEARCH_URL_TEMPLATE, gameId, limit);
        String data = searchTwitch(url);
        List<Item> videos = getItemList(data);
        for(Item item : videos){
            item.setItemType(ItemType.VIDEO);
        }
        return videos;
    }

    // key: itemType    value: items
    public Map<String, List<Item>> searchItems(String gameId) throws TwitchException{
        Map<String, List<Item>> itemMap = new HashMap<>();
        for(ItemType type : ItemType.values()){
            /*
            type[] types = [stream, video, clip]
              List<Item> items = searchByType(gameId, type, limits);
              itemMap.put(type.toString(), items);
             */
            itemMap.put(type.toString(), searchByType(gameId, type, DEFAULT_GAME_LIMIT));
        }
        return itemMap;
    }

    public  List<Item> searchByType(String gameId, ItemType type, int limit) throws TwitchException{ // stream
        List<Item> items = Collections.emptyList();
        switch(type){
            //Enums are often used in switch statements to check for corresponding values:
            case STREAM:
                items = searchStreams(gameId, limit);
                break;
            case VIDEO:
                items = searchVideos(gameId, limit);
                break;
            case CLIP:
                items = searchClips(gameId, limit);
                break;
        }
        // video response do not return gameId, so we have to manually add gameId to video Item
        for(Item item : items){
            item.setGameId(gameId);
        }
        return items;
    }
}
