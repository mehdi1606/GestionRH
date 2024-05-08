package com.innovx.gestionrh.security.jwt;


import com.innovx.gestionrh.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  // Set the length of the secret key (adjust as needed)
  private static final int SECRET_KEY_LENGTH = 64;




  private String jwtSecret = generateSecretKey();

  @Value("${innovx.app.jwtExpirationMs}")
  private int jwtExpirationMs;




  // Generate a random secret key
  private String generateSecretKey() {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[SECRET_KEY_LENGTH];
    random.nextBytes(bytes);
    return Base64.getEncoder().encodeToString(bytes);
  }
  public class SecretKeyGenerator {
    public static void main(String[] args) {
      SecureRandom random = new SecureRandom();
      byte[] bytes = new byte[64]; // Adjust the byte length as needed
      random.nextBytes(bytes);
      String secretKey = Base64.getEncoder().encodeToString(bytes);
      System.out.println("Generated Secret Key: " + secretKey);
    }
  }

  public String generateJwtToken(Authentication authentication) {
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    return Jwts.builder()
            .setSubject(userPrincipal.getEmail())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(key(), SignatureAlgorithm.HS256) // Change to HS256
            .compact();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
            .parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
  public String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7); // Remove "Bearer " prefix and return only the Token
    }
    return null;
  }
}
