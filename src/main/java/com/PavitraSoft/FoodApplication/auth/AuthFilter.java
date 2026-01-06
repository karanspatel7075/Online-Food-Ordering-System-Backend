package com.PavitraSoft.FoodApplication.auth;

import com.PavitraSoft.FoodApplication.globalController.UnauthorizedException;
import com.PavitraSoft.FoodApplication.model.User;
import com.PavitraSoft.FoodApplication.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j
public class AuthFilter extends OncePerRequestFilter {

    private final AuthTokenGen authTokenGen;
    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public AuthFilter(AuthTokenGen authTokenGen, UserRepository userRepository, HandlerExceptionResolver handlerExceptionResolver) {
        this.authTokenGen = authTokenGen;
        this.userRepository = userRepository;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            String path = request.getServletPath();

            // ✅ Skip auth APIs
            if (path.startsWith("/api/auth")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String requestTokenHeader = request.getHeader("Authorization");

            if(requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = requestTokenHeader.substring(7);
            String email = authTokenGen.getUserFromToken(token);

            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findByEmail(email).orElseThrow();

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("Token expired");
        }
    }
}
