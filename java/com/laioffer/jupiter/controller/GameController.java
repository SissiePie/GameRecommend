package com.laioffer.jupiter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.jupiter.service.GameService;
import com.laioffer.jupiter.service.TwitchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
// controller 里头也有@component
// this annotation is to 注册我们 url and method
// so when the request comes in servlet will know dispatch the task to where
public class GameController {

    private final GameService gameService;
    @Autowired
    // implement injection through constructor
    public GameController(GameService gameService){
        this.gameService = gameService;
    }


   @RequestMapping(value = "/game", method = RequestMethod.GET)
   // define the element value game, element method:get
   // we search game so is to get the game info;
   //requestParam value is the whatever game name or exact game name;,
   // required is does it must exist,
   // default is true , yes has to be,
   // false is if it doesn't exist then return null;
   //HttpServletResponse response   通过HttpServletResponse response 回去。
    public void getGame(@RequestParam(value = "game_name", required = false) String gameName,
       HttpServletResponse response)throws IOException, ServletException {
        // the response will return to the frontend as the json style
       response.setContentType("application/json;charset=UTF-8");
       try {
           // Return the dedicated game information if gameName is provided in the request URL, otherwise return the top x games.
           if (gameName != null) {
               response.getWriter().print(new ObjectMapper().writeValueAsString(gameService.searchGame(gameName)));
           } else {
               response.getWriter().print(new ObjectMapper().writeValueAsString(gameService.topGames(0)));
           }
       } catch (TwitchException e) {
           throw new ServletException(e);
       }
   }
}
