package com.example.restaurantserver.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {
   void getAuthToken(HttpServletRequest request, HttpServletResponse response,
                     String authentication,
                     long hour) throws Exception;
}
