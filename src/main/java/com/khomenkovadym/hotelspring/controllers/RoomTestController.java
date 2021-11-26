package com.khomenkovadym.hotelspring.controllers;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = { "/roomses" })
@Slf4j
public class RoomTestController {
    @GetMapping
    public String getRooms() {
        return "index2";
    }

    @GetMapping(value = "/{roomId}")
    public String getRoomById(@PathVariable Integer roomId) {
        return "index";
    }

    @PostMapping(value = "/{roomId}/booking")
    public String bookRoom(@PathVariable Integer roomId, @ModelAttribute RoomBookingDTO dto) {

        if ("Vadym".equals(dto.getHello())) {
            log.info("Hello, Anton");
        }
        return "index2";
    }

    @Data
    public class RoomBookingDTO {
        private String hello;
        private String time;
    }
}
