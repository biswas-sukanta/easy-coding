
package com.sukanta.easycoding.gatewayservice.security;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

	private final Environment environment;

	@Autowired
	public AuthorizationHeaderFilter(Environment environment) {
		super(Config.class);
		this.environment = environment;
	}

	public static class Config {
		// set config
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest httpRequest = exchange.getRequest();
			log.info("From gateway-Service -> Local Address : {} ", httpRequest.getLocalAddress());
			ServerHttpResponse response = exchange.getResponse();
			if (!httpRequest.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				response.setStatusCode(HttpStatus.UNAUTHORIZED);
				return response.setComplete();
			}

			String authHeader = Objects.requireNonNull(httpRequest.getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
			String token = authHeader.replace(Objects.requireNonNull(environment.getProperty("auth.token.header.prefix")), "");
			if (isJwtNotValid(token)) {
				response.setStatusCode(HttpStatus.UNAUTHORIZED);
				return response.setComplete();
			}
			return chain.filter(exchange);
		};
	}

	private boolean isJwtNotValid(String token) {
		String subject;
		try {
			subject = Jwts.parser().setSigningKey(environment.getProperty("token.secret")).parseClaimsJws(token)
					.getBody().getSubject();
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			log.info("Authentication Failed");
			return Boolean.TRUE;
		}
		return subject == null || subject.isEmpty();
	}
}