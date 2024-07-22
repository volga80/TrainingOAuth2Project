package com.example.TrainingOAuth2.config;

import com.example.TrainingOAuth2.error.CustomAccessDeniedHandler;
import com.example.TrainingOAuth2.error.CustomAuthenticationFailureHandler;
import com.example.TrainingOAuth2.error.CustomLogoutSuccessHandler;
import com.example.TrainingOAuth2.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomAccessDeniedHandler customAccessDeniedHandler, CustomAuthenticationFailureHandler customAuthenticationFailureHandler, CustomLogoutSuccessHandler customLogoutSuccessHandler) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/", "/login").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .failureHandler(customAuthenticationFailureHandler)
                )
                .logout(logout -> logout
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                )
                .exceptionHandling(exceptions -> {
                            try {
                                exceptions
                                        .accessDeniedHandler(customAccessDeniedHandler)
                                        .and()
                                        .exceptionHandling()
                                        .accessDeniedPage("/error");
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .oidcUserService(customOAuth2UserService)
                        )
                        .defaultSuccessUrl("/profile", true)
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
