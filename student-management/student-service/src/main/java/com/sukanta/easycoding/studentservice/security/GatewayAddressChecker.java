package com.sukanta.easycoding.studentservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class GatewayAddressChecker {
    public boolean isLocalHost(Authentication authentication, HttpServletRequest request) {
        log.info("Server name : {}",  request.getServerName());
        return "localhost".equals(request.getServerName());
    }
}