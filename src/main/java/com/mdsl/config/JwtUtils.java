package com.mdsl.config;

import java.util.Date;
import java.util.function.Function;

import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.mdsl.service.UserDetailsImpl;

import io.jsonwebtoken.*;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	@Autowired
	private JwtProperties jwtProperties;
	
	public String generateJwtToken(Authentication authentication, boolean isRefresh) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		
		Claims claims = Jwts.claims().setSubject(userPrincipal.getUsername());
        claims.put("userId", userPrincipal.getId() + "");
 //       claims.put("instId", userPrincipal.getInstId() + "");
 //       claims.put("branchId", userPrincipal.getBranchId() + "");
        claims.put("userLevel", userPrincipal.getUserLevel() + "");
		
        return Jwts
        		.builder()
        		.setHeaderParam("typ", "JWT")
        		.setClaims(claims)
        		.setIssuedAt(new Date())
        		.setExpiration(new Date((new Date()).getTime() + (isRefresh ? jwtProperties.getRefreshExpirationMs() : jwtProperties.getExpirationMs())))
				.signWith(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()), SignatureAlgorithm.HS512).compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()))
				.build()
				.parseClaimsJws(token)
				.getBody().getSubject();
	}
	
	//check if the token has expired
	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	//for retrieving any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		//return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes())).build().parseClaimsJws(token).getBody();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes())).build().parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
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

	public Claims parseAndValidate(String token) {
		return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes())).build().parseClaimsJws(token)
				.getBody();
	}
}