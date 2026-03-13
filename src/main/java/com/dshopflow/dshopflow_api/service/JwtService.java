package com.dshopflow.dshopflow_api.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.expiration}")
    private long expiration;

    //generate token for a user
    public String generateToken(String email,
                                String role,
                                Long shopId
                                ){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("shopId", shopId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(
                        System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(),
                        SignatureAlgorithm.HS256)
                .compact();

    }
    //Extract email from token
    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //Extract role from token
    public String extractRole(String token){
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // Validate token
    public boolean isTokenValid(String token, String email){
        final String tokenEmail  = extractEmail(token);
        return tokenEmail.equals(email) && !isTokenExpired(token);
    }

    // check if token is expired
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic claim extractor
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSigningKey(){
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
