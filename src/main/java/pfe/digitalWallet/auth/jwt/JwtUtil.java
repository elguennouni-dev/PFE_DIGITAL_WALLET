package pfe.digitalWallet.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class JwtUtil {
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;
    @Value("${jwt.expire.time}")
    private long EXPIRE_TIME;

    // Repeated things ...
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String CLAIM_USERNAME = "username";


    // Strip bearer prefix from token
    private String stripBearerPrefix(String token) {
        return token.replace(BEARER_PREFIX, "");
    }

    // Generate key for jwt
    private Key getKey() {
        try {
            byte[] keyBytes = SECRET_KEY.getBytes();
            return Keys.hmacShaKeyFor(keyBytes);
        }catch (Exception e) {
            throw new JwtException("Failed to get key", e);
        }
    }

    // Token creation
    private String createToken(Map<String, Object> claimsMap, String username) {
        Date now = new Date(System.currentTimeMillis());
        Date expireDate = new Date(now.getTime() + EXPIRE_TIME);
        return Jwts.builder()
                .setClaims(claimsMap)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getKey())
                .compact();
    }

    // Extract claims
    private Optional<Claims> getClaimsFromToken(String token) {
        if (!isValidToken(token))
            return Optional.empty();

        String stripedToken = stripBearerPrefix(token);
    
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(stripedToken)
                    .getBody();
            return Optional.of(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }



    // Public main methods
    public String generateToken(String username) {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(CLAIM_USERNAME, username);
        return createToken(claimsMap, username);
    }

    public String getUsernameFromToken(String token) {
        if(!isValidToken(token))
            return null;
        Optional<Claims> claims = getClaimsFromToken(token);
        if(claims.isEmpty())
            return null;
        return claims.orElse(null).get(CLAIM_USERNAME, String.class);
    }

    public Date getExpireDateFromToken(String token) {
        if (!isValidToken(token))
            return null;

        Optional<Claims> claims = getClaimsFromToken(token);
        if(claims == null)
            return null;
        return (Date) claims.orElse(null).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        if(!isValidToken(token))
            return true;
        Optional<Claims> claims = getClaimsFromToken(token);
        if(claims == null)
            return true;
        return claims.orElse(null).getExpiration().before(new Date(System.currentTimeMillis()));
    }

    public boolean validateToken(String token, String username) {
        if(!isValidToken(token))
            return false;
        String tokenUsername = getUsernameFromToken(token);
        return tokenUsername != null && tokenUsername.equals(username) && !isTokenExpired(token);
    }

    public boolean isValidToken(String token) {
        return token != null && token.startsWith(BEARER_PREFIX) && getClaimsFromToken(token).isPresent();
    }




    // Get id from token in future...


}