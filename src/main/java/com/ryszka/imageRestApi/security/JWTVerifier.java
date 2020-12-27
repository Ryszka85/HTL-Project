package com.ryszka.imageRestApi.security;

import io.jsonwebtoken.Jwts;

public class JWTVerifier {
    private String token;
    private String secret;

    public JWTVerifier(String token, String secret) {
        this.token = token;
        this.secret = secret;
    }

    public String verifyToken() {
        String token = this.token.replace("Bearer ", "");
        System.out.println(token);
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }



}
