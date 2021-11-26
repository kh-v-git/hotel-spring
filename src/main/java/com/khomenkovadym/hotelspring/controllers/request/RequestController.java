package com.khomenkovadym.hotelspring.controllers.request;

import com.khomenkovadym.hotelspring.model.RequestView;
import com.khomenkovadym.hotelspring.model.RoomRequestDTO;
import com.khomenkovadym.hotelspring.model.UserData;
import com.khomenkovadym.hotelspring.services.RoomRequestService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = {"/request"})
@Slf4j
public class RequestController {
    private final ModelMapper modelMapper = new ModelMapper();

    private RoomRequestService requestService;

    @Autowired
    public void setRequestService(RoomRequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping(value = {"/user"})
    public String request(Model model) {
        List<RoomRequestDTO> roomRequestList = new ArrayList<>();
        model.addAttribute("requestList", roomRequestList);
        LocalDate nowDate = LocalDate.now();
        model.addAttribute("tomDate", nowDate.plusDays(1));
        model.addAttribute("nowDate", nowDate);
        model.addAttribute("roomRequest", new RequestView());
        return "user/request_user";
    }
}
