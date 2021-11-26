package com.khomenkovadym.hotelspring.controllers.security;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Controller
@RequestMapping(value = {"/login"})
@Slf4j
public class LoginController {

    @GetMapping
    public String login() {
        return "/security/login";
    }

    @PostMapping(value = "/auth")
    public String authUser(@ModelAttribute @Valid UserLoginDTO loginDTO
            ) {

        String loginEmail = loginDTO.getUserEmail();
        if ("user@user.com".equals(loginEmail)) {
            return "user/request_user";
        }
        if ("man@man.com".equals(loginEmail)) {
            return "/user/index_manager";
        }
        if ("admin@admin.com".equals(loginEmail)) {
            return "/user/index_admin";
        }
        return "requests_user";
    }

    @Data
    public class UserLoginDTO {
        @Email
        private String userEmail;
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
        private String userPassword;
    }
}
