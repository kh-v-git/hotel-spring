package com.khomenkovadym.hotelspring.controllers;

import com.khomenkovadym.hotelspring.model.CustomUserDetails;
import com.khomenkovadym.hotelspring.model.UserRequestDTO;
import com.khomenkovadym.hotelspring.model.RoomRequestDTO;
import com.khomenkovadym.hotelspring.services.RoomRequestService;
import com.khomenkovadym.hotelspring.services.RoomService;
import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import com.mysql.cj.exceptions.WrongArgumentException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(value = {"/request"})
@Slf4j
public class RequestController {
    private final RoomRequestService requestService;
    private final RoomService roomService;

    @Autowired
    public RequestController(RoomRequestService requestService, RoomService roomService) {
        this.requestService = requestService;
        this.roomService = roomService;
    }


    @GetMapping(value = {"/user"})
    public String showRequestByUser(Model model, HttpServletRequest request) {
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<RoomRequestDTO> roomReqDTO = requestService.getRoomRequestListWithRoomNumberByUser(customUser.getUserId());

        LocalDate nowDate = LocalDate.now();

        int maxAdults = roomService.findMaxAdultCapacity();
        int maxChildren = roomService.findMaxChildCapacity();
        List<RoomBedSize> bedSizeList = roomService.getAllAvailableBedSized();

        request.getSession().setAttribute("maxAdultCapacity",maxAdults );
        request.getSession().setAttribute("maxChildCapacity", maxChildren);
        request.getSession().setAttribute("roomBedSizeList", bedSizeList);
        model.addAttribute("tomDate", nowDate.plusDays(1));
        model.addAttribute("nowDate", nowDate);
        model.addAttribute("requestDTOList", roomReqDTO);
        return "user/request_user";
    }

    @PostMapping(value = {"/user/create"})
    public String setRequestByUser(@Valid @ModelAttribute UserRequestDTO userRequestForm) throws NotFoundException {

        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        requestService.saveRequest(userRequestForm, customUser.getUserId());

        return "redirect:/request/user";
    }
    @PostMapping(value = {"/user/remove"})
    public String deleteRequestByUser(@RequestParam("requestId") Integer requestId){
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            requestService.removeRequestByIdUserId(requestId, customUser.getUserId());
        } catch (NotFoundException e) {
            log.debug("User request not deleted", e);
        }
        return "redirect:/request/user";
    }

    @PostMapping(value = {"/user/decline"})
    public String declineRequestByUser(@RequestParam("requestId") Integer requestId) {
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            requestService.declineUserRequestById(requestId, customUser.getUserId());
        } catch (NotFoundException e) {
            log.debug("Decline request by user failed", e);
        }

        return "redirect:/request/user";
    }

    @PostMapping(value = {"/user/approve"})
    public String approveRequestByUser(@RequestParam("requestId") Integer requestId) {
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            requestService.approveUserRequestById(requestId, customUser.getUserId());
        } catch (NotFoundException e) {
            log.debug("Decline request by user failed", e);
        }

        return "redirect:/request/user";
    }

    @GetMapping(value = {"/manager"})
    private String showRequestToManager (Model model) {
        List<RoomRequestDTO> roomReqDTO = requestService.getAllRoomRequestList();
        model.addAttribute("requestDTOList", roomReqDTO);
        return "manager/request_manager";
    }

    @PostMapping(value = {"/manager/remove"})
    public String deleteRequestByManager(@RequestParam("requestId") Integer requestId) {
        requestService.removeRequestById(requestId);
        return "redirect:/request/manager/";
    }

    @PostMapping(value = {"/manager/decline"})
    public String declineRequestByManager(@RequestParam("requestId") Integer requestId) {
        try {
            requestService.declineRequestById(requestId);
        } catch (NotFoundException e) {
            log.debug("Request update by manager failed", e);
        }
        return "redirect:/request/manager/";
    }
}
