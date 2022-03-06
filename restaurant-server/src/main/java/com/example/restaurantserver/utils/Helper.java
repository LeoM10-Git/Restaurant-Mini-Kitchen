package com.example.restaurantserver.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

@Component
public class Helper {

    public Algorithm algorithmBuilder() throws Exception{
        return Algorithm.HMAC256("secret".getBytes());
    }

    public String decodedJWT(String refreshToken) throws Exception {
        JWTVerifier verifier = JWT.require(algorithmBuilder()).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        return decodedJWT.getSubject();
    }
}
