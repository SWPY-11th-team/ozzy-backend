package com.example.ozzy.common.filter;

import com.example.ozzy.common.UserContext;
import com.example.ozzy.oauth2.util.JwtTokenUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter implements Filter {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String token = httpRequest.getHeader("Authorization");
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new IllegalArgumentException("Refresh token has expired. Please login again.");
        }

        int userId = jwtTokenUtil.getUserId(token);
        UserContext.setUserId(userId);

        chain.doFilter(request, response);
    }

}
