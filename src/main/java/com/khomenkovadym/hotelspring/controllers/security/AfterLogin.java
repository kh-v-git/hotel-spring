package com.khomenkovadym.hotelspring.controllers.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
@RequestMapping(value = {"/afterLogin"})
public class AfterLogin {

    @GetMapping
    public String afterLogin(HttpServletRequest request) {
        if (request.isUserInRole("ADMIN")) {
            return "redirect:/user/admin/users";
        }
        if (request.isUserInRole("USER")) {
            return "redirect:/request/user";
        }
        if (request.isUserInRole("MANAGER")) {
            return "redirect:/request/manager";
        }
        return "redirect:/index";
    }
}
