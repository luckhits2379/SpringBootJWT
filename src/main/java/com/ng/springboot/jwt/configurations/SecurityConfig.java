package com.ng.springboot.jwt.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	@Autowired
	private JWTFilter jWTFilter;

	@Autowired
	private AuthException authException;

	//@formatter:off
	@Bean @SuppressWarnings({ "deprecation"})
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.csrf().disable()
		.authorizeRequests()
		.requestMatchers("/public/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.httpBasic()
		.and()
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.exceptionHandling((ex)-> ex.authenticationEntryPoint(authException));

		httpSecurity.addFilterBefore(jWTFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();

	}
	
}
