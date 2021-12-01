package com.khomenkovadym.hotelspring.controllers.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@Slf4j
@RequestMapping(value = {"/login"})
public class LoginController {

    @GetMapping
    public String login(@RequestParam(required = false) boolean loginError, Model model) {
        if (loginError) {
            model.addAttribute("loginError", true);
        }
        return "/security/login";
    }
}
