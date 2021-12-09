package com.khomenkovadym.hotelspring.controllers;

import com.khomenkovadym.hotelspring.entities.Room;
import com.khomenkovadym.hotelspring.model.RoomView;
import com.khomenkovadym.hotelspring.services.RoomService;
import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = {"/", "/index"})
@Slf4j
public class IndexController {

    private final RoomService roomService;

    @Autowired
    public IndexController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        List<Room> roomList = roomService.getRoomsByRoomIdNotNull();
        Map<String, RoomView> roomViews = new HashMap<>();

        for (Room room : roomList) {
            String bedSize = room.getBedSize().toString();
            RoomView roomView = roomViews.get(bedSize);
            double price = room.getPrice();
            int adultCapacity = room.getAdultCapacity();
            int childrenCapacity = room.getChildCapacity();
            if (roomView == null) {
                roomViews.put(bedSize, new RoomView(bedSize, price, price, 0, 1, 0, 0));
                continue;
            }
            if (roomView.getMaxPrice() < price) {
                roomView.setMaxPrice(price);
            }
            if (roomView.getMinPrice() > price) {
                roomView.setMinPrice(price);
            }
            if (roomView.getMaxAdults() < adultCapacity) {
                roomView.setMaxAdults(adultCapacity);
            }
            if (roomView.getMinAdults() > adultCapacity) {
                roomView.setMinAdults(adultCapacity);
            }
            if (roomView.getMaxChildren() < childrenCapacity) {
                roomView.setMaxChildren(childrenCapacity);
            }
            if (roomView.getMinChildren() > childrenCapacity) {
                roomView.setMinChildren(childrenCapacity);
            }
        }
        List<RoomView> roomViewList = new ArrayList<>(roomViews.values());

        if (roomList.isEmpty()) {
            log.info("Hotel has no rooms");
        }
        model.addAttribute("roomViewList", roomViewList);
        int maxAdults = roomService.findMaxAdultCapacity();
        int maxChildren = roomService.findMaxChildCapacity();
        List<RoomBedSize> bedSizeList = roomService.getAllAvailableBedSized();

        model.addAttribute("maxAdultCapacity", maxAdults );
        model.addAttribute("maxChildCapacity", maxChildren);
        model.addAttribute("roomBedSizeList", bedSizeList);
        request.getSession().setAttribute("maxAdultCapacity",maxAdults );
        request.getSession().setAttribute("maxChildCapacity", maxChildren);
        request.getSession().setAttribute("roomBedSizeList", bedSizeList);
        return "index";
    }
}
