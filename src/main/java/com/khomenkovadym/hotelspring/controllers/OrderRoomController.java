package com.khomenkovadym.hotelspring.controllers;

import com.khomenkovadym.hotelspring.model.CustomUserDetails;
import com.khomenkovadym.hotelspring.model.RoomOrderDTO;
import com.khomenkovadym.hotelspring.model.UserRequestDTO;
import com.khomenkovadym.hotelspring.services.RoomOrderService;
import com.khomenkovadym.hotelspring.utils.RoomOrderStatus;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/order")
@Slf4j
public class OrderRoomController {
    private final RoomOrderService roomOrderService;

    @Autowired
    public OrderRoomController(RoomOrderService roomOrderService) {
        this.roomOrderService = roomOrderService;
    }

    @ModelAttribute("userRequestDTO")
    public UserRequestDTO userRequestDTO() {
        return new UserRequestDTO();
    }

    @GetMapping(value = "/user")
    public String ordersUser(Model model) {
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

    @PostMapping(value = "/user/remove")
    public String deleteOrder(@RequestParam("orderId") Integer orderId) {
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            roomOrderService.removeRoomOrderByUserById(orderId, customUser.getUserId());
        } catch (NotFoundException e) {
            log.debug("Delete order failed", e);
        }

        return "redirect:/order/user";
    }

    @PostMapping(value = "/user/decline")
    public String declineOrder(@RequestParam("orderId") Integer orderId) {
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            roomOrderService.declineRoomOrderById(orderId, customUser.getUserId());
        } catch (NotFoundException e) {
            log.debug("Update order failed", e);
        }
        return "redirect:/order/user";
    }

    @GetMapping(value = "/manager")
    public String ordersManager(Model model) {
        List<RoomOrderDTO> roomOrderDTOList = new ArrayList<>();
        try {
            roomOrderDTOList = roomOrderService.findActiveUserOrders(RoomOrderStatus.EXPIRED);
        } catch (NotFoundException ex) {
            log.debug("No user orders found", ex);
        }
        model.addAttribute("orderDTOList", roomOrderDTOList);
        return "manager/order_manager";
    }

    @PostMapping(value = {"/manager/remove"})
    public String deleteOrderManager(@RequestParam("orderId") Integer orderId) {
            roomOrderService.removeRoomOrderById(orderId);
        return "redirect:/order/manager";
    }

}
