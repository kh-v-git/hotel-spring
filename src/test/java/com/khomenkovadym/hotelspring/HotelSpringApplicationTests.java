package com.khomenkovadym.hotelspring;

import com.khomenkovadym.hotelspring.entities.*;
import com.khomenkovadym.hotelspring.repositories.RoomOrderRepository;
import com.khomenkovadym.hotelspring.repositories.RoomRepository;
import com.khomenkovadym.hotelspring.repositories.RoomRequestRepository;
import com.khomenkovadym.hotelspring.repositories.UserRepository;
import com.khomenkovadym.hotelspring.services.RoomService;
import com.khomenkovadym.hotelspring.utils.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles(profiles = {"test"})
class HotelSpringApplicationTests {

    @Autowired
    RoomService roomService;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomOrderRepository roomOrderRepository;

    @Autowired
    RoomRequestRepository roomRequestRepository;

    @Autowired
    UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
    @Transactional
    void testLoadRooms() {

        Room room = Room.builder()
            .number(1)
            .about("Some text")
            .adultCapacity(123)
            .bedSize(RoomBedSize.COT)
            .childCapacity(0)
            .build();

        Room savedRoom = roomRepository.saveAndFlush(room);
        assertNotNull(savedRoom.getRoomId());

        Page<Room> rooms = roomService.getRooms(Pageable.ofSize(100), 1);
        assertNotNull(rooms);


        User user = userRepository.saveAndFlush(User.builder()
            .email("user@user.com")
            .phone("231321231231")
            .password("323212313213")
            .firstName("John")
            .lastName("John")
            .role(UserRoleEnum.USER)
            .status(UserStatusEnum.ACTIVE)
            .build());

        assertNotNull(user.getUserId());

        RoomRequest roomRequest = roomRequestRepository.saveAndFlush(RoomRequest.builder()
            .user(user)
            .adultsCapacity(3)
            .bedSize(RoomBedSize.DOUBLE)
            .childrenCapacity(0)
            .status(RoomRequestStatus.REQUESTED)
            .arrivalDate(LocalDate.now())
            .departureDate(LocalDate.now())
            .build());

        assertNotNull(roomRequest.getRequestId());

        RoomOrder roomOrder = roomOrderRepository.saveAndFlush(RoomOrder.builder()
            .roomRequest(roomRequest)
            .user(user)
            .room(room)
            .arrivalDate(LocalDate.now())
            .departureDate(LocalDate.now())
            .status(RoomOrderStatus.OFFERED)
            .orderDate(LocalDateTime.now())
            .build());

        assertNotNull(roomOrder.getOrderId());
    }
}
