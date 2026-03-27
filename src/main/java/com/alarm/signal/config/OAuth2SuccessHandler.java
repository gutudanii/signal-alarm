package com.alarm.signal.config;

import com.alarm.signal.user.dto.request.CreateUserRequest;
import com.alarm.signal.user.model.User;
import com.alarm.signal.user.model.enums.AuthProvider;
import com.alarm.signal.user.model.enums.Role;
import com.alarm.signal.user.services.UserService;
import com.alarm.signal.user.repository.UserRepository;
import com.alarm.signal.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String email = user.getAttribute("email");
        String firstName = user.getAttribute("given_name");
        String lastName = user.getAttribute("family_name");

        if (email == null || email.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Email not provided by Google\"}");
            return;
        }

        userRepository.findByEmailIgnoreCase(email)
            .ifPresentOrElse(
                existingUser -> {
                    // Existing user: do nothing (login)
                },
                () -> {
                    // New user: create with provider=GOOGLE, isEmailVerified=true, default role USER
                    userService.createUser(
                        CreateUserRequest.builder()
                            .email(email)
                            .firstName(firstName)
                            .lastName(lastName)
                            .provider(AuthProvider.GOOGLE)
//                            .role(Collections.singleton(Role.USER))
//                            .isEmailVerified(true)
                            .build()
                    );
                }
            );

        // Find user and get role for JWT
        User dbUser = userRepository.findByEmailIgnoreCase(email).orElse(null);
        String roles = dbUser != null && dbUser.getRoles() != null
                ? dbUser.getRoles().stream().map(Enum::name).collect(Collectors.joining(","))
                : "USER";
        String token = jwtService.generateToken(email, roles);

        // Optional: handle mobile deep link redirect
        String deepLink = request.getParameter("redirect");
        if (deepLink != null && deepLink.startsWith("myapp://")) {
            response.sendRedirect(deepLink + "?token=" + token);
            return;
        }
        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + token + "\"}");
    }
}
