package com.khomenkovadym.hotelspring.controllers.security;

import com.khomenkovadym.hotelspring.model.UserData;
import com.khomenkovadym.hotelspring.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping(value = {"/registration"})
@Slf4j
public class RegistrationController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String registration(Model model) {
        model.addAttribute("userData", new UserData());
        return "security/registration";
    }

    @PostMapping("/register")
    public String userRegistration(@Valid UserData userData, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("registrationForm", userData);
            return "security/registration";
        }
        /*
        try {
            userService.register(userData);
        }catch (UserAlreadyExistException e){
            bindingResult.rejectValue("email", "userData.email","An account already exists for this email.");
            model.addAttribute("registrationForm", userData);
            return "redirect:/login";
        }
        return "redirect:/index";
         */
        return "redirect:/index";
    }

}
