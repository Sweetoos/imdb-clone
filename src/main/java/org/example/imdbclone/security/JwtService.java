package org.example.imdbclone.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
//    private final SecretKey key;
//    private final long expirationTime;
//
//    public JwtService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationTime){
//        this.key= Keys.hmacShaKeyFor(secret.getBytes());
//        this.expirationTime=expirationTime;
//    }
//
//    public String generateToken(String username){
//        return Jwts.builder()
//                .subject(username)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis()+expirationTime))
//                .signWith(key)
//                .compact();
//    }
}
