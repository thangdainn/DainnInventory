package org.dainn.dainninventory.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.Pair;
import org.dainn.dainninventory.service.security.CustomUserDetailService;
import org.dainn.dainninventory.utils.enums.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private  CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exception;


    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) {
        try {
            if (isByPassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
                String userName = jwtProvider.getEmailFromJwt(jwt);
                String provider = jwtProvider.getProviderFromJwt(jwt);
                UserDetails userDetails = customUserDetailService.loadUserByUsernameAndProvider(userName, Provider.valueOf(provider));
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            exception.resolveException(request, response, null, e);
        }

    }

    private boolean isByPassToken(HttpServletRequest request) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        final List<Pair<String, String>> byPassToken = Arrays.asList(
                new Pair<>("/register", "POST"),
                new Pair<>("/refresh-token", "POST"),
                new Pair<>("/login", "POST"),
                new Pair<>("/login/**", "POST")
        );
        String uri = request.getRequestURI();
        String method = request.getMethod();
        for (Pair<String, String> pair : byPassToken) {
            if (antPathMatcher.match(pair.a, uri) && antPathMatcher.match(pair.b, method)) {
                return true;
            }
        }
        return false;
    }
}
