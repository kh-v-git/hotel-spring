package com.khomenkovadym.hotelspring.controllers.security;

import com.khomenkovadym.hotelspring.entities.User;
import com.khomenkovadym.hotelspring.model.UserRegistrationDTO;
import com.khomenkovadym.hotelspring.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(value = {"/registration"})
@Slf4j
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegistrationDTO userRegistrationDto() {
        return new UserRegistrationDTO();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "security/registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDTO registrationDTO,
                                      BindingResult result) {

        Optional<User> maybeUser = userService.findByEmail(registrationDTO.getEmail());

        if (maybeUser.isPresent()) {
            result.rejectValue("email", "empty", "User exist. Login!");
        }
        if (result.hasErrors()) {
            return "security/registration";
        }

        userService.saveAndFlush(registrationDTO);
        return "redirect:/login";
    }

}
