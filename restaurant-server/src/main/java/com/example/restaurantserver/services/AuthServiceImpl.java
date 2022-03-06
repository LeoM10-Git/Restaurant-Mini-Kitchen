package com.example.restaurantserver.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.restaurantserver.models.UserData;
import com.example.restaurantserver.models.UserRole;
import com.example.restaurantserver.utils.Helper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{
    private final Helper helper;
    private final UserService userService;

    @Override
    public void getAuthToken(HttpServletRequest request, HttpServletResponse response,
                             String authorizationHeader, long minutes) throws Exception {
        String access_token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = helper.algorithmBuilder();
        String email = helper.decodedJWT(access_token);
        UserData user = userService.getUserByEmail(email);

        String refresh_token = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() +  minutes  * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",
                        user.getRoles().stream().map(UserRole::getRoleName).collect(Collectors.toList()))
                .sign(algorithm);
        Map<String, String> token = new HashMap<>();
        token.put("access_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), token);
    }
}
