package com.finnplay.config;

import com.finnplay.exception.BussinessException;
import com.finnplay.repo.UserRepository;
import com.finnplay.service.JwtTokenService;
import com.finnplay.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        var token = tokenService.resolveToken(request);
        var dbToken = tokenService.validateToken(token);
        if (dbToken != null && jwtTokenService.isTokenValid(token,dbToken.getEmail())) {
            var userDetails = userRepository.findByEmail(dbToken.getEmail()).orElseThrow(() -> new BussinessException("email not exist"));
            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            handlerExceptionResolver.resolveException(request, response, null, new BussinessException("token invalidate"));
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/api/users/register") || request.getRequestURI().startsWith("/api/users/login");
    }
}
