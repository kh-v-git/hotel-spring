package com.khomenkovadym.hotelspring.controllers.room;

import com.khomenkovadym.hotelspring.entities.Room;
import com.khomenkovadym.hotelspring.model.RoomDTO;
import com.khomenkovadym.hotelspring.services.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


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

    @GetMapping(value = "/{roomId}")
    public String getRoomListByBedSize(@PathVariable Integer roomId, Model model) {
        Room r = roomService.getRoomByRoomID(roomId);
        RoomDTO rDTO = new RoomDTO();
        modelMapper.map(r, rDTO);
        model.addAttribute("room", rDTO);
        return "main/room";
    }
}
