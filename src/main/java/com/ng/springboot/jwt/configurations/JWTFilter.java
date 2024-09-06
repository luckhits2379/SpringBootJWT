package com.ng.springboot.jwt.configurations;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtility jwtUtility;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		System.out.println("Inside JWT Filter");

		String requestHeader = request.getHeader("Authorization");
		// Bearer 2352345235sdfrsfgsdfsdf

		String username = null;
		String token = null;

		if (requestHeader != null && requestHeader.startsWith("Bearer")) {

			token = requestHeader.substring(7);

			try {

				username = this.jwtUtility.getUsernameFromToken(token);

			} catch (IllegalArgumentException e) {

				System.out.println("Illegal Argument while fetching the username !!");
				e.printStackTrace();

			} catch (ExpiredJwtException e) {

				System.out.println("Given jwt token is expired !!");
				e.printStackTrace();

			} catch (MalformedJwtException e) {

				System.out.println("Some changed has done in token !! Invalid Token");
				e.printStackTrace();

			} catch (Exception e) {

				e.printStackTrace();

			}

		} else {

			System.out.println("Invalid Header Value !! ");
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			// fetch user detail from username
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			System.out.println("token expiration date: " + this.jwtUtility.getExpirationDateFromToken(token));

			Boolean validateToken = this.jwtUtility.validateToken(token, userDetails);

			if (validateToken) {

				// set the authentication
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);

			} else {

				System.out.println("Validation fails !!");
			}

		}

		System.out.println("done from JWT Filter");

		filterChain.doFilter(request, response);

	}
}
