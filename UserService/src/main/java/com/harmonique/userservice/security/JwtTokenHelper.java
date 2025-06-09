package com.harmonique.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenHelper {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenHelper.class);

    // Secret key for signing JWT, loaded from application properties
    @Value("${jwt.secret}")
    private String secret;

    // Token validity duration in milliseconds (e.g., 1 hour = 3600000 ms)
    @Value("${jwt.token-validity}")
    private long tokenValidity;

    /**
     * Generates a JWT token for the given user details.
     * Adds user's roles as claims in the token.
     * 
     * @param userDetails UserDetails object containing username and roles
     * @return Signed JWT token string
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Extract roles from user authorities and add as claim
        var roles = userDetails.getAuthorities()
                               .stream()
                               .map(auth -> auth.getAuthority())
                               .toList();

        claims.put("roles", roles);

        logger.debug("Generating token for user: {}, roles: {}", userDetails.getUsername(), roles);

        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Creates the JWT token using claims, subject (username), issue and expiry time.
     * Signs the token using HS512 algorithm and secret key.
     * 
     * @param claims Claims to include in the token payload
     * @param username Subject (usually username)
     * @return Signed JWT token string
     */
    private String createToken(Map<String, Object> claims, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenValidity);

        logger.debug("Creating token with expiry: {}", expiryDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Validates the token against the provided user details.
     * Checks if username matches and token is not expired.
     * 
     * @param token JWT token string
     * @param userDetails UserDetails to validate against
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        logger.debug("Validating token for user: {}. Is valid? {}", username, isValid);
        return isValid;
    }

    /**
     * Extracts username (subject) from the token.
     * 
     * @param token JWT token string
     * @return username stored in token
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Checks if the token has expired.
     * 
     * @param token JWT token string
     * @return true if expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        Date expiration = getClaimFromToken(token, Claims::getExpiration);
        boolean expired = expiration.before(new Date());
        logger.debug("Token expiration date: {}, is expired? {}", expiration, expired);
        return expired;
    }

    /**
     * Generic method to extract any claim from the token using a resolver function.
     * 
     * @param <T> Type of claim to return
     * @param token JWT token string
     * @param claimsResolver Function to extract desired claim from Claims object
     * @return extracted claim value
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses and returns all claims present in the JWT token.
     * 
     * @param token JWT token string
     * @return Claims object containing all token claims
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extracts the roles claim from the token.
     * 
     * @param token JWT token string
     * @return List of roles as strings
     */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        List<String> roles = claims.get("roles", List.class); // returns List<String>
        logger.debug("Extracted roles from token: {}", roles);
        return roles;
    }
}