package com.ng.springboot.jwt.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class UserConfig {

	@Bean
	UserDetailsService userDetailsService() {
		UserDetails userDetails = User.builder().username("admin").password(passwordEncoder().encode("admin"))
				.roles("ADMIN").build();
		return new InMemoryUserDetailsManager(userDetails);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {

		return builder.getAuthenticationManager();
	}

}
