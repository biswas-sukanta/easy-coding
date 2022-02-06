package com.sukanta.easycoding.gatewayservice.security;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class RouteLocatorConfig {

    private static final String LB_STUDENT_SERVICE = "lb://student-service";
    private static final String STUDENT_SERVICE_SEGMENT = "/student-service/(?<segment>.*)";
    private static final String REPLACEMENT_SEGMENT = "/$\\{segment}";
    public static final String COOKIE = "Cookie";

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder rlb, AuthorizationHeaderFilter
            authorizationHeaderFilter) {

        return rlb
                .routes()
                .route(p -> p
                        .path("/student-service/student/login").and().method(HttpMethod.POST)
                        .filters(f -> f.removeRequestHeader(COOKIE)
                                .rewritePath(STUDENT_SERVICE_SEGMENT, REPLACEMENT_SEGMENT))
                        .uri(LB_STUDENT_SERVICE))
                .route(p -> p
                        .path("/student-service/postSampleData").and().method(HttpMethod.GET)
                        .filters(f -> f.removeRequestHeader(COOKIE)
                                .rewritePath(STUDENT_SERVICE_SEGMENT, REPLACEMENT_SEGMENT))
                        .uri(LB_STUDENT_SERVICE))
                .route(p -> p
                        .path("/student-service/findStudentByFirstName/**").and().method(HttpMethod.GET)
//                      .and().header(HttpHeaders.AUTHORIZATION, "Bearer (.*)")
                        .filters(f -> f.removeRequestHeader(COOKIE)
                                .rewritePath(STUDENT_SERVICE_SEGMENT, REPLACEMENT_SEGMENT)
                                .filter(authorizationHeaderFilter.apply(new
                                        AuthorizationHeaderFilter.Config())))
                        .uri(LB_STUDENT_SERVICE))
                .route(p -> p
                        .path("/address-service/findAddressByEmail").and().method(HttpMethod.GET)
                        .filters(f -> f.removeRequestHeader(COOKIE)
                                .rewritePath("/address-service/(?<segment>.*)", REPLACEMENT_SEGMENT))
                        .uri("lb://address-service"))
                .build();
    }
}