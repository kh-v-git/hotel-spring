package com.khomenkovadym.hotelspring.controllers.room;

import com.khomenkovadym.hotelspring.entities.Room;
import com.khomenkovadym.hotelspring.model.RoomDTO;
import com.khomenkovadym.hotelspring.services.RoomService;
import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping(value = {"/room"})
@Slf4j
public class RoomController {
    private final ModelMapper modelMapper = new ModelMapper();

    private RoomService roomService;

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String getRooms() {
        return "index";
    }

    @GetMapping(value = "/view/{roomId}")
    public String getRoomListByBedSize(@PathVariable Integer roomId, Model model, Principal principal) {
        Room r = roomService.getRoomByRoomID(roomId);
        RoomDTO rDTO = new RoomDTO();
        modelMapper.map(r, rDTO);
        model.addAttribute("room", rDTO);
        return "main/room";
    }
    @GetMapping(value = "/category/{bedSize}")
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
}
