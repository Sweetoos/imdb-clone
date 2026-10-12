package org.example.imdbclone.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationTime;
    public JwtService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationTime) {
        key = secret.isBlank() ? Jwts.SIG.HS256.key().build() : Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        if (expirationTime <= 0) throw new IllegalArgumentException("JWT expiration must be positive");
        this.expirationTime = expirationTime;
    }
    public String generateToken(Long userId) {
        Date now = new Date();
        return Jwts.builder().subject(userId.toString()).issuedAt(now)
                .expiration(new Date(now.getTime() + expirationTime)).signWith(key).compact();
    }
    public Long extractUserId(String token) {
        return Long.valueOf(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject());
    }
}
