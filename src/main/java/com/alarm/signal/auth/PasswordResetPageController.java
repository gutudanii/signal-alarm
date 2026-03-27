package com.alarm.signal.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordResetPageController {

    @GetMapping("/api/auth/reset-password")
    public String showPasswordResetPage(@RequestParam(value = "token", required = false) String token, Model model) {
        // Optionally add the token to the model if you want to use it in Thymeleaf
        model.addAttribute("token", token);
        return "password-reset-page"; // Thymeleaf template name (without .html)
    }
}

