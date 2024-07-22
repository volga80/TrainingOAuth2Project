package com.example.TrainingOAuth2.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUser() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "Test User");
        OAuth2User principal = new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "name"
        );

        mockMvc.perform(get("/user").with(oauth2Login().oauth2User(principal)))
                .andExpect(status().isOk())
                .andExpect(content().string("Добро пожаловать, Test User"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAdmin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Приветствую повелителя данного ресурса"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAdminAccessDeniedForUser() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testProfile() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "Test User");
        attributes.put("email", "testuser@example.com");
        OAuth2User principal = new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "name"
        );

        MvcResult result = mockMvc.perform(get("/profile").with(oauth2Login().oauth2User(principal)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assert(responseContent.contains("profile:"));
        assert(responseContent.contains("Test User"));
        assert(responseContent.contains("testuser@example.com"));
    }
}