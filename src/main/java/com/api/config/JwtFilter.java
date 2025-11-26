package com.api.config;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.api.authservice.impl.JwtService;
import com.api.authservice.impl.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private ApplicationContext context;

	  private Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info("Inside doFilterInternal method of JwtFilter");
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;
		
		logger.info("AuthHeader: "+authHeader);
		
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			logger.info("Check auth Header and Bearer: "+ token);
			token = authHeader.substring(7);
			logger.info("token: "+token);
			username = jwtService.extractUsername(token);
			logger.info("Extract username from token");
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = context
					.getBean(MyUserDetailsService.class).loadUserByUsername(username);
				logger.info("Load user name");

			if (jwtService.validateToken(token, userDetails)) {
				logger.info("Token is valid, setting authentication in context");
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				logger.info("Authentication Token: " + authToken);
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
		logger.info("Exit from doFilterInternal method of JwtFilter");
	}

}
