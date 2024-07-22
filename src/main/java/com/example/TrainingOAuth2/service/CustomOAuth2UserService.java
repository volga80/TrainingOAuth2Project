package com.example.TrainingOAuth2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
public class CustomOAuth2UserService extends OidcUserService {

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        log.info("Вызван метод loadUser");

        OidcUser oidcUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oidcUser.getAttributes();
        String email = (String) attributes.get("email");

        SimpleGrantedAuthority authority;
        if ("volgin26@gmail.com".equals(email)) {
            authority = new SimpleGrantedAuthority("ROLE_ADMIN");
            log.info("Вход админа");
        } else {
            authority = new SimpleGrantedAuthority("ROLE_USER");
            log.info("Вход пользователя");
        }

        OidcUserInfo oidcUserInfo = new OidcUserInfo(attributes);

        return new DefaultOidcUser(Collections.singletonList(authority),
                userRequest.getIdToken(), oidcUserInfo);
    }
}
