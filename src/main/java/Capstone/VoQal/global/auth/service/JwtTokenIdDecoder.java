package Capstone.VoQal.global.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenIdDecoder {
    private final HttpServletRequest request;
    private final JwtProvider jwtProvider;

    public long extractIdFromTokenInHeader() {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            return extractIdFromToken(token);
        } else {
            throw new IllegalArgumentException("Token not found in header.");
        }
    }

    public long extractIdFromToken(String token) {
        Key signingKey = jwtProvider.getSigningKey();

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
            String idString = claims.getBody().get("jti", String.class);
            return Long.parseLong(idString);
        } catch (JwtException | IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Error extracting ID from token.");
        }
    }
}
