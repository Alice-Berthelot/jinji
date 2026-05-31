package com.jinji.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Value("${secret_jwt}")
    private String secretJwt;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretJwt.getBytes());
    }

    public String generateAccessToken(UserDetails userDetails) {

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .toList();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)
                .claim("type", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 min
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7 days
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        try {
            return parse(token).getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public Date extractExpiration(String token) {
        try {
            return parse(token).getExpiration();
        } catch (JwtException e) {
            return null;
        }
    }

    public boolean isAccessToken(String token) {
        try {
            return "access".equals(parse(token).get("type", String.class));
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        try {
            return "refresh".equals(parse(token).get("type", String.class));
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isValidAccessToken(String token, UserDetails userDetails) {
        try {
            Claims claims = parse(token);

            return "access".equals(claims.get("type", String.class))
                    && claims.getSubject().equals(userDetails.getUsername())
                    && claims.getExpiration().after(new Date());

        } catch (JwtException e) {
            return false;
        }
    }
}