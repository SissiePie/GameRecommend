package com.laioffer.jupiter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.jupiter.entity.Request.LoginRequestBody;
import com.laioffer.jupiter.entity.response.LoginResponseBody;
import com.laioffer.jupiter.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    // HttpServletRequest has a method getSession() ;
    // HttpServletResponse will return the response of sessionId
    public void login(@RequestBody LoginRequestBody requestBody,
                      HttpServletRequest request, HttpServletResponse response) throws
            IOException {
        String firstname = loginService.verifyLogin(requestBody.getUserId(), requestBody.getPassword());
        // create a new session for user if userId and password are correct
        if(!firstname.isEmpty()){
            // create a new session, put userId as an attribute into the session object,
            // and set the expiration time to 600 seconds
            HttpSession session = request.getSession();
            session.setAttribute("user_id", requestBody.getUserId());
            session.setMaxInactiveInterval(600);

            LoginResponseBody loginResponseBody = new LoginResponseBody(requestBody.getUserId(),
                    firstname);
            response.setContentType("application/json;charset =UTF-8 ");
            response.getWriter().print(new ObjectMapper().writeValueAsString(loginResponseBody));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}

/*

if you login with the right password then we will return a session id store in the local cookie
if it's not correct then we will return 401 error

    client/frontend passing user's UserId, password ----> Jason
    Deserilize(@RequestBody here we create a LoginRequestBody class) --> we will get userId and password;
    verify the user exist or not ---> call method verifyLogin in loginService class (by dependency injection)
    verifyLogin method is calling the LoginDao to verify if the userId exist
    check the user is exsit by checking the userId;
    if user exist && password we got from user's input is matched; then we create the new session
    return the userId and firstName, serilize by the @responseBody to Jason form and return to frontend;
    if user not exist, we will get a empty name, then we gonna return a 401 error.

    Log out
    clear session and set the cookie into null;


    Tomcat has session container,

 */