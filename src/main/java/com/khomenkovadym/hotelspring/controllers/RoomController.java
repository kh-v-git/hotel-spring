package com.khomenkovadym.hotelspring.controllers;

import com.khomenkovadym.hotelspring.entities.Room;
import com.khomenkovadym.hotelspring.entities.RoomRequest;
import com.khomenkovadym.hotelspring.model.*;
import com.khomenkovadym.hotelspring.services.RoomOrderService;
import com.khomenkovadym.hotelspring.services.RoomRequestService;
import com.khomenkovadym.hotelspring.services.RoomService;
import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import com.khomenkovadym.hotelspring.utils.RoomOrderStatus;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping(value = {"/room"})
@Slf4j
public class RoomController {
    private static final String USER_ORDER_ROOM = "user/order_room_user";

    private final ModelMapper modelMapper = new ModelMapper();
    private final RoomService roomService;
    private final RoomOrderService roomOrderService;
    private final RoomRequestService roomRequestService;

    @Autowired
    public RoomController(RoomService roomService, RoomOrderService roomOrderService, RoomRequestService roomRequestService) {
        this.roomService = roomService;
        this.roomOrderService = roomOrderService;
        this.roomRequestService = roomRequestService;
    }

    @ModelAttribute("userRequestDTO")
    public UserRequestDTO userRequestDTO() {
        return new UserRequestDTO();
    }

    @GetMapping
    public String getRooms() {
        return "index";
    }

    @GetMapping(value = {"/view/{roomId}"})
    public String getRoomListByBedSize(@PathVariable Integer roomId, Model model) {
        Room r = roomService.getRoomByRoomID(roomId);
        RoomDTO rDTO = new RoomDTO();
        modelMapper.map(r, rDTO);
        model.addAttribute("room", rDTO);
        return "main/room";
    }

    @GetMapping(value = {"/category/{bedSize}"})
    public String getRoomListByBedSize(@PathVariable RoomBedSize bedSize, Model model) {
        List<Room> roomList = roomService.findAllRoomsByBedSize(bedSize);

        int maxAdultsCapacity = roomList.stream().mapToInt(Room::getAdultCapacity).max().orElse(1);
        int maxChildrenCapacity = roomList.stream().mapToInt(Room::getChildCapacity).max().orElse(0);

        model.addAttribute("roomList", roomList);
        model.addAttribute("bedSize", bedSize);
        model.addAttribute("maxAdultsCapacity", maxAdultsCapacity);
        model.addAttribute("maxChildCapacity", maxChildrenCapacity);
        return "main/rooms";
    }

    @GetMapping(value = {"/user/order-room"})
    public String showRoomListForUser(Model model) {

        LocalDate nowDate = LocalDate.now();
        model.addAttribute("tomDate", nowDate.plusDays(1));
        model.addAttribute("nowDate", nowDate);
        return USER_ORDER_ROOM;
    }

    @GetMapping(value = {"/user/find-room"})
    public Object showRoomListForRequest(@ModelAttribute("userRequestDTO") @Valid UserRequestDTO userRequestDTO
            , BindingResult result, RedirectAttributes attributes, HttpSession session) {

        if (result.hasErrors() || (userRequestDTO.getDepartureDate().isBefore(userRequestDTO.getArrivalDate()))) {
            return USER_ORDER_ROOM;
        }

        List<RoomDTO> roomDTOList = roomService.findFreeRoomsForRequest(userRequestDTO);

        attributes.addFlashAttribute("userRequestDTO", userRequestDTO);
        attributes.addFlashAttribute("roomStatus", RoomOrderStatus.FREE);
        attributes.addFlashAttribute("roomOrderDTOList", roomDTOList);
        session.setAttribute("userRequestDTO", userRequestDTO);
        return new RedirectView("/room/user/order-room");
    }

    @PostMapping(value = {"/user/order-room"})
    public String orderRoom(@RequestParam("roomId") Integer roomId, HttpSession session) {
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserRequestDTO requestDto = (UserRequestDTO) session.getAttribute("userRequestDTO");
        try {
            roomOrderService.addRoomOrder(customUser.getUserId(), roomId, requestDto);
        } catch (NotFoundException e) {
            log.debug("Room order not saved", e);
        }

        session.removeAttribute("userRequestDTO");
        return "redirect:/order/user";
    }


    @GetMapping(value = {"/manager"})
    public String showRoomsForManager(Model model, HttpSession session) {
        int maxAdults = roomService.findMaxAdultCapacity();
        int maxChildren = roomService.findMaxChildCapacity();
        List<RoomBedSize> bedSizeList = roomService.getAllAvailableBedSized();

        session.setAttribute("maxAdultCapacity", maxAdults);
        session.setAttribute("maxChildCapacity", maxChildren);
        session.setAttribute("roomBedSizeList", bedSizeList);
        LocalDate nowDate = LocalDate.now();
        model.addAttribute("tomDate", nowDate.plusDays(1));
        model.addAttribute("nowDate", nowDate);
        return "manager/order_room_manager";
    }

    @GetMapping(value = {"/manager/find-room"})
    public Object showRoomsForRequest(@ModelAttribute("userRequestDTO") @Valid UserRequestDTO userRequestDTO
            , BindingResult result, RedirectAttributes attributes, HttpSession session) {

        if (result.hasErrors() || (userRequestDTO.getDepartureDate().isBefore(userRequestDTO.getArrivalDate()))) {
            return "manager/order_room_manager";
        }
        List<RoomDTO> roomDTOList = roomService.findFreeRoomsForRequest(userRequestDTO);

        attributes.addFlashAttribute("action", "LOCK");
        attributes.addFlashAttribute("userRequestDTO", userRequestDTO);
        attributes.addFlashAttribute("roomStatus", RoomOrderStatus.FREE);
        attributes.addFlashAttribute("roomOrderDTOList", roomDTOList);
        session.setAttribute("userRequestDTO", userRequestDTO);
        return new RedirectView("/room/manager");
    }

    @GetMapping(value = {"/manager/find-request-room"})
    public Object findRoomsForRequest(RedirectAttributes attributes, HttpSession session, @RequestParam("requestId") Optional<Integer> maybeRequestId) {
        if (maybeRequestId.isEmpty()) {
            return "manager/order_room_manager";
        }
        try {
            UserRequestDTO reqDTO = roomRequestService.findUserRequestDTOById(maybeRequestId.get());
            List<RoomDTO> roomDTOList = roomService.findFreeRoomsForRequest(reqDTO);

            attributes.addFlashAttribute("userRequestDTO", reqDTO);
            attributes.addFlashAttribute("roomOrderDTOList", roomDTOList);
            attributes.addFlashAttribute("requestId", maybeRequestId.get());
            attributes.addFlashAttribute("action", "BOOK");
            attributes.addFlashAttribute("roomStatus", RoomOrderStatus.FREE);
            session.setAttribute("userRequestDTO", reqDTO);
        } catch (NotFoundException e) {
            log.debug("User request not found by ID", e);
            return "manager/order_room_manager";
        }
        session.setAttribute("requestId", maybeRequestId.get());
        return new RedirectView("/room/manager");
    }

    @PostMapping(value = {"/manager/order-room/lock"})
    public String lockRoomManager(@RequestParam("roomId") Integer roomId, HttpSession session) {
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserRequestDTO requestDto = (UserRequestDTO) session.getAttribute("userRequestDTO");
        try {
            roomOrderService.lockRoomByManager(customUser.getUserId(), roomId, requestDto);
        } catch (NotFoundException e) {
            log.debug("Room order not saved", e);
        }
        session.removeAttribute("userRequestDTO");
        return "redirect:/order/manager";
    }

    @PostMapping(value = {"/manager/order-room/assign"})
    public String assignRoomManager(@RequestParam("roomId") Integer roomId, @RequestParam("requestId") Integer requestId, HttpSession session) {
        UserRequestDTO requestDto = (UserRequestDTO) session.getAttribute("userRequestDTO");
        try {
            roomRequestService.assignRoomToRequestByManager(requestId, roomId, requestDto);
        } catch (NotFoundException e) {
            log.debug("Room not assigned to Request by Id", e);
        }
        session.removeAttribute("userRequestDTO");
        return "redirect:/request/manager";
    }

    @GetMapping(value = {"/admin/rooms"})
    public String showRoomListAdmin(Model model, @RequestParam(defaultValue = "1") Integer pageNo) {

        int recordsPerPage = 5;
        long numOfRooms = roomService.getRoomAmount();

        long numOfPages = numOfRooms / recordsPerPage;
        if (numOfRooms % recordsPerPage > 0) {
            numOfPages++;
        }

        List<RoomDTO> roomDTOList = roomService.findAllRooms(pageNo - 1, recordsPerPage);

        model.addAttribute("pageNo", pageNo);
        model.addAttribute("numOfPages", numOfPages);
        model.addAttribute("roomDTOList", roomDTOList);
        return "admin/rooms_admin";
    }


    @GetMapping(value = {"/admin/room-add"})
    public String formRoomAdmin(Model model, HttpSession session) {

        session.setAttribute("bedSizeList", RoomBedSize.RoomBedSizeList());
        model.addAttribute("roomDTO", new RoomDTO());
        return "admin/add_room_admin";
    }

    @PostMapping(value = {"/admin/room-add"}, params = "action=create")
    public String createRoomAdmin(@ModelAttribute("roomDTO") @Valid RoomDTO roomDTO
            , BindingResult result, HttpSession session) {

        if (result.hasErrors()) {
            return "admin/add_room_admin";
        }

        roomService.createRoom(roomDTO);

        session.removeAttribute("bedSizeList");
        return "redirect:/room/admin/rooms";
    }

}

