package com.khomenkovadym.hotelspring.controllers.room;

import com.khomenkovadym.hotelspring.entities.Room;
import com.khomenkovadym.hotelspring.services.RoomService;
import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = {"/rooms"})
@Slf4j
public class RoomsController {
    private RoomService roomService;

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String getRooms() {
        return "index";
    }

    @GetMapping(value = "/{bedSize}")
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
