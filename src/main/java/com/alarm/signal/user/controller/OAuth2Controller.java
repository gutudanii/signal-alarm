package com.alarm.signal.user.controller;

import com.alarm.signal.user.dto.request.CreateUserRequest;
import com.alarm.signal.user.model.enums.AuthProvider;
import com.alarm.signal.user.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth/oauth2")
@RequiredArgsConstructor
@Tag(name = "OAuth2", description = "Google OAuth2 user creation endpoint")
public class OAuth2Controller {
    private final UserService userService;

    @Operation(summary = "Create a user with Google OAuth2 if not exists")
    @GetMapping("/create")
    public String createUserWithGoogle(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> attributes = principal.getAttributes();
        String email = (String) attributes.get("email");
        String firstName = (String) attributes.getOrDefault("given_name", "");
        String lastName = (String) attributes.getOrDefault("family_name", "");

        boolean exists = userService.existsByEmail(email).isExists();
        if (exists) {
            return "User already exists";
        }
        userService.createUser(
                CreateUserRequest.builder()
                        .email(email)
                        .firstName(firstName)
                        .lastName(lastName)
                        .provider(AuthProvider.GOOGLE)
                        .build()
        );
        return "User created";
    }
}
