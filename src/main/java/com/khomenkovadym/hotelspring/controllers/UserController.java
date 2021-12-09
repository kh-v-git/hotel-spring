package com.khomenkovadym.hotelspring.controllers;

import com.khomenkovadym.hotelspring.model.CustomUserDetails;
import com.khomenkovadym.hotelspring.model.UserDTO;
import com.khomenkovadym.hotelspring.services.UserService;
import com.khomenkovadym.hotelspring.utils.UserRoleEnum;
import com.khomenkovadym.hotelspring.utils.UserStatusEnum;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = {"/user"})
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserDTO userRegistrationDto() {
        return new UserDTO();
    }

    @GetMapping(value = {"/user/about"})
    public String request(Model model) {
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO;
        try {
            userDTO = userService.findUserById(customUser.getUserId());
        } catch (NotFoundException e) {
            log.debug("User not found by Id", e);
            return "redirect:/user/about_user";
        }
        model.addAttribute("user", userDTO);
        return "user/about_user";
    }

    @PostMapping(value = {"/user/update"}, params = "action=update")
    public String updateUserData(@ModelAttribute("user") @Valid UserDTO userDTO,
                                 BindingResult result) {
        if (result.hasErrors()) {
            return "user/about_user";
        }
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            userService.updateUserById(customUser.getUserId(), userDTO);
        } catch (NotFoundException e) {
            log.debug("User not found by id", e);
            return "redirect:/request/user";
        }

        return "redirect:/user/user/about";
    }

    @PostMapping(value = {"/user/update"}, params = "action=delete")
    public String updateUserData() {
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            userService.deleteUserById(customUser.getUserId());
        } catch (NotFoundException e) {
            log.debug("User not found by id", e);
            return "redirect:/user/about_user";
        }
        return "redirect:/user/user/about";
    }

    @GetMapping(value = {"/admin/users"})
    public String showUserListAdmin(Model model, HttpSession session) {
        List<UserDTO> userDTOList = (List<UserDTO>) session.getAttribute("userList");
        if (null == userDTOList) {
            model.addAttribute("userDTOList", userService.findAllUsers());
        } else {
            model.addAttribute("userDTOList", userDTOList);
        }
        session.removeAttribute("userList");
        session.setAttribute("userRoleList", UserRoleEnum.getUserRoleList());
        session.setAttribute("userStatusList", UserStatusEnum.getUserStatusList());
        return "admin/user_admin";
    }

    @PostMapping(value = {"/admin/user-remove"})
    public String removeUserAdminByID(@RequestParam("userId") Integer userId) {
        try {
            userService.deleteUserById(userId);
        } catch (NotFoundException e) {
            log.debug("User delete by Id failed", e);
        }

        return "redirect:/user/admin/users";
    }

    @GetMapping(value = {"/admin/search-user"})
    public String userSearchAdminByEmail(@RequestParam("searchEmail") String email, HttpSession session) {
        List<UserDTO> userDTOList = userService.findAllByEmailLike(email);
        session.setAttribute("userList", userDTOList);
        return "redirect:/user/admin/users";
    }

    @GetMapping(value = {"/admin/user-about"})
    public String userAboutAdminById(Model model, @RequestParam("userId") Integer userId) {
        try {
            UserDTO userDTO = userService.findUserById(userId);
            model.addAttribute("user", userDTO);
        } catch (NotFoundException e) {
            log.debug("User not found by id", e);
            return "redirect:/user/admin/users";
        }
        return "admin/user_about_admin";
    }

    @PostMapping(value = {"/admin/user-update"})
    public String updateUserAboutAdminById(@ModelAttribute("user") @Valid UserDTO userDTO,
                                           BindingResult result, @RequestParam("userId") Integer userId, HttpSession session) {
        if (result.hasErrors()) {
            return "admin/user_about_admin";
        }
        try {
            userService.updateUserAdminById(userId, userDTO);
        } catch (NotFoundException e) {
            log.debug("User not found by id", e);
        }
        session.removeAttribute("userRoleList");
        session.removeAttribute("userStatusList");
        return "redirect:/user/admin/users";

    }
}
