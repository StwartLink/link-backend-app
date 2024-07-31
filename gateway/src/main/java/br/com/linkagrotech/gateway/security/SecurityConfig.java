package br.com.linkagrotech.gateway.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private String issuerUri = "http://localhost:8081/realms/agrobrasil";

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
	http.csrf((Customizer<CsrfSpec>) CsrfSpec::disable)
		.authorizeExchange(exchange -> exchange.pathMatchers(HttpMethod.GET, "/webjars/**").permitAll()
			.pathMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
			.pathMatchers(HttpMethod.GET, "/webjars/swagger-ui/index.html").permitAll()
			.pathMatchers(HttpMethod.GET, "/swagger-resources/**").permitAll()
			.pathMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
			.pathMatchers(HttpMethod.POST, "/auth/**").permitAll().pathMatchers(HttpMethod.POST)
			.authenticated().pathMatchers(HttpMethod.PUT).authenticated().pathMatchers(HttpMethod.DELETE)
			.authenticated().pathMatchers(HttpMethod.PATCH).authenticated().anyExchange().authenticated())
		.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtDecoder(jwtDecoder())));
	return http.build();
    }

    @Bean
    ReactiveJwtDecoder jwtDecoder() {
	return ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
	CorsConfiguration corsConfig = new CorsConfiguration();
	corsConfig.applyPermitDefaultValues();
	corsConfig.addAllowedMethod(HttpMethod.PUT);
	corsConfig.addAllowedMethod(HttpMethod.DELETE);
	corsConfig.addAllowedMethod(HttpMethod.GET);
	corsConfig.addAllowedMethod(HttpMethod.POST);
	corsConfig.addAllowedMethod(HttpMethod.OPTIONS);
	corsConfig.setAllowedOrigins(Arrays.asList("http://localhost", issuerUri, "*"));

	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	source.registerCorsConfiguration("/**", corsConfig);
	return source;
    }

}
