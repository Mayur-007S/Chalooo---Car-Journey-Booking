package com.api.authservice.impl;


import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.api.controller.UserController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService{
	
	private String secretKey = "";
	
	  private Logger logger = LoggerFactory.getLogger(JwtService.class);
	
	public JwtService() throws NoSuchAlgorithmException {
		
		KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
		SecretKey key = keyGenerator.generateKey();
		secretKey = Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
//	public String generateToken(String username) {
//		logger.info("Generating token for user: " + username);
//		Map<String, Object> claims = new HashMap<>();
//		logger.info("Exiting generateToken method");
//		return Jwts.builder()
//				.claims()
//				.add(claims)
//				.subject(username)
//				.issuedAt(new Date(System.currentTimeMillis()))
//				.expiration(java.sql.Timestamp.valueOf(
//					    java.time.LocalDateTime.now().plusMonths(6)
//				))
//				.and()
//				.signWith(getKey())
//				.compact();   
//	}
	
	public String generateToken(String username) {
		logger.info("Generating token for user: " + username);
	    return Jwts.builder()
	        .subject(username)
	        .issuedAt(new Date())
	        .expiration(Date.from(
	            LocalDateTime.now()
	                .plusMonths(6)
	                .atZone(ZoneId.systemDefault())
	                .toInstant()
	        ))
	        .signWith(getKey(), Jwts.SIG.HS256)
	        .compact();
	}


//	private SecretKey getKey() {
//		logger.info("Getting secret key for JWT");
//		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//		return Keys.hmacShaKeyFor(keyBytes);
//	}
//	
	private SecretKey getKey() {
	    return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

//	private Claims extractAllClaims(String token) {
//		// TODO Auto-generated method stub
//		return Jwts.parser()
//				.verifyWith(getKey())
//				.build()
//				.parseSignedClaims(token)
//				.getPayload();
//	}
	
	private Claims extractAllClaims(String token) {
		logger.info("Inside  extract all  claims  method.");
	    if (token.startsWith("Bearer ")) {
	        token = token.substring(7);
	    }

	    return Jwts.parser()
	        .verifyWith(getKey())
	        .build()
	        .parseSignedClaims(token)
	        .getPayload();
	}


	public boolean validateToken(String token, UserDetails userDetail) {
		final String username = extractUsername(token);
		return (username.equals(userDetail.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	
}