package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.exception.InvalidTokenTypeException;
import ar.edu.utn.frba.dds.models.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Getter
    private final Key key;

    private static final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000; // 15 minutos
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 días
    private static final String ISSUER = "auth-service";
    private static final String AUDIENCE = "api";

    public JwtUtil(@Value("${jwt.secret.key}") String secret) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        key = Keys.hmacShaKeyFor(decodedKey);
    }


    public String generarAccessToken(User user) {
        return Jwts.builder()
            .setSubject(user.getId().toString())
            .setIssuer(ISSUER)
            .setAudience(AUDIENCE)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
            .claim("type", "access")
            .claim("role", user.getRole().name())
            .claim("permissions", user.getPermissions().stream()
                .map(Enum::name)
                .toList())
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }


    public String generarRefreshToken(User user) {
        return Jwts.builder()
            .setSubject(user.getId().toString())
            .setIssuer(ISSUER)
            .setAudience(AUDIENCE)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
            .claim("type", "refresh")
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String getUserIdFromAccessToken(String token) {
        validateTokenType(token, "access");
        return getClaim(token, Claims::getSubject);
    }

    public String getUserIdFromRefreshToken(String token) {
        validateTokenType(token, "refresh");
        return getClaim(token, Claims::getSubject);
    }

    private void validateTokenType(String token, String expectedType) {
        String type = getClaim(token, c -> c.get("type", String.class));
        if (!expectedType.equals(type)) {
            throw new InvalidTokenTypeException();
        }
    }


    private Claims getAllClaims(String token){
        return Jwts
            .parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public List<String> getRoleAndPermissions(String token) {
        List<String> authorities = new ArrayList<>();

        authorities.add("ROLE_" + getRole(token)) ;

        List<String> permissions = getPermissions(token).stream()
            .map(p -> "CAN_" + p.toString())
            .toList();
        authorities.addAll(permissions);
        return authorities;
    }
    private String getRole(String token){
        Claims claims = getAllClaims(token);
        return claims.get("role", String.class);
    }
    private List<?> getPermissions(String token){
        Claims claims = getAllClaims(token);
        return claims.get("permissions", List.class);
    }
}
