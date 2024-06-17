package com.pepe.miniapp.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Long telegramId = obtainTelegramId(request);
        String hash = obtainHash(request);
        CustomAuthenticationToken authRequest = new CustomAuthenticationToken(telegramId, hash);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Nullable
    protected Long obtainTelegramId(HttpServletRequest request) {
        return Long.valueOf(request.getParameter("telegramId"));
    }

    @Nullable
    protected String obtainHash(HttpServletRequest request) {
        return request.getParameter("hash");
    }
}
