package com.example.TrainingOAuth2.error;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CustomAuthenticationFailureHandlerTest {


    @Test
    public void testOnAuthenticationFailure() throws IOException, ServletException {
        CustomAuthenticationFailureHandler handler = new CustomAuthenticationFailureHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        handler.onAuthenticationFailure(request, response, new org.springframework.security.core.AuthenticationException("") {});

        assertEquals("/login?error", response.getRedirectedUrl());
    }
}