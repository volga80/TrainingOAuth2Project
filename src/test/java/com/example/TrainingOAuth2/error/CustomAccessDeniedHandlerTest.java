package com.example.TrainingOAuth2.error;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CustomAccessDeniedHandlerTest {

    @Test
    public void testHandle() throws IOException, ServletException {
        CustomAccessDeniedHandler handler = new CustomAccessDeniedHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        handler.handle(request, response, new org.springframework.security.access.AccessDeniedException(""));

        assertEquals("/error", response.getRedirectedUrl());
    }
}