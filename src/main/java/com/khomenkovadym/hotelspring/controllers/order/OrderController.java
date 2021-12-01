package com.khomenkovadym.hotelspring.controllers.order;

import com.khomenkovadym.hotelspring.model.CustomUserDetails;
import com.khomenkovadym.hotelspring.model.RoomOrderDTO;
import com.khomenkovadym.hotelspring.services.RoomOrderService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = {"/order"})
@Slf4j
public class OrderController {
    private RoomOrderService roomOrderService;

    public OrderController(RoomOrderService roomOrderService) {
        this.roomOrderService = roomOrderService;
    }

    @GetMapping(value = {"/user"})
    public String request(Model model) {
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<RoomOrderDTO> roomOrdDTO = new ArrayList<>();
        try {
            roomOrdDTO = roomOrderService.findUserOrdersByUser(customUser.getUserId());
        } catch (NotFoundException ex) {
            log.debug("No user orders found", ex);
        }
        LocalDate nowDate = LocalDate.now();
        model.addAttribute("tomDate", nowDate.plusDays(1));
        model.addAttribute("nowDate", nowDate);
        model.addAttribute("orderDTOList", roomOrdDTO);
        return "user/order_user";
    }

    @PostMapping(value = {"/user/remove"})
    public String deleteOrder(@RequestParam("orderId") Integer orderId) throws NotFoundException {

        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        roomOrderService.removeRoomOrderById(orderId, customUser.getUserId());
        return "redirect:/order/user";
    }
}
