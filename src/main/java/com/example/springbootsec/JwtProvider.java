package com.example.springbootsec;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final String ROLES_KEY = "roles";

    private String secretKey;
    private long validityInMilliseconds;

    public JwtProvider(@Value("${security.jwt.token.secret-key}") String secretKey,
                       @Value("${security.jwt.token.expiration}") long validityInMilliseconds) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
    }


    public String createToken(String username, List<UserAuthority> roles) {
        Claims claims = Jwts.claims().setSubject(username);

        claims.put(ROLES_KEY, roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.authority))
                .collect(Collectors.toList())
        );

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public List<GrantedAuthority> getRoles(String token) {
        List<Map<String, String>> list = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get(ROLES_KEY, List.class);
        return list.stream()
                .map(roleClaim -> new SimpleGrantedAuthority(roleClaim.get("authority")))
                .collect(Collectors.toList());
    }

}
