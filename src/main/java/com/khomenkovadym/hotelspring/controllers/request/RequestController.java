package com.khomenkovadym.hotelspring.controllers.request;

import com.khomenkovadym.hotelspring.model.CustomUserDetails;
import com.khomenkovadym.hotelspring.model.UserRequestDTO;
import com.khomenkovadym.hotelspring.model.RoomRequestDTO;
import com.khomenkovadym.hotelspring.services.RoomRequestService;
import com.khomenkovadym.hotelspring.services.RoomService;
import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
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
    private RoomRequestService requestService;
    private RoomService roomService;

    public RequestController(RoomRequestService requestService, RoomService roomService) {
        this.requestService = requestService;
        this.roomService = roomService;
    }


    @GetMapping(value = {"/user"})
    public String request(Model model, HttpServletRequest request) {
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
    public String setRequest(@Valid @ModelAttribute UserRequestDTO userRequestForm) throws NotFoundException {

        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        requestService.saveRequest(userRequestForm, customUser.getUserId());

        return "redirect:/request/user";
    }
    @PostMapping(value = {"/user/remove"})
    public String deleteRequest(@RequestParam("requestId") Integer requestId)  throws NotFoundException {

        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        requestService.removeRequestById(requestId, customUser.getUserId());

        return "redirect:/request/user";
    }
}
