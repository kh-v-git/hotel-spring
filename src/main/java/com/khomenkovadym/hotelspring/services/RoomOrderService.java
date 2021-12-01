package com.khomenkovadym.hotelspring.services;

import com.khomenkovadym.hotelspring.entities.RoomOrder;
import com.khomenkovadym.hotelspring.entities.RoomRequest;
import com.khomenkovadym.hotelspring.entities.User;
import com.khomenkovadym.hotelspring.model.RoomOrderDTO;
import com.khomenkovadym.hotelspring.repositories.RoomOrderRepository;
import com.khomenkovadym.hotelspring.repositories.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomOrderService {
    private RoomOrderRepository roomOrderRepository;
    private UserRepository userRepository;

    @Autowired
    public RoomOrderService(RoomOrderRepository roomOrderRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.roomOrderRepository = roomOrderRepository;
    }

    @Transactional
    public Page<RoomOrder> getRoomOrders(Pageable pageable, Integer number) {
        return roomOrderRepository.findAll(pageable);
    }

    @Transactional
    public List<RoomOrderDTO> findUserOrdersByUser(Integer userId) throws NotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        List<RoomOrder> roomOrderList = roomOrderRepository.findAllByUser(userOptional.get());
        if (roomOrderList.isEmpty()) {
            throw new NotFoundException("User booking list is empty");
        }
        List<RoomOrderDTO> roomOrderDTOList = new ArrayList<>();
        for (RoomOrder order : roomOrderList) {
            RoomOrderDTO roomOrderDTO = RoomOrderDTO.builder()
                    .orderId(order.getOrderId())
                    .userId(order.getUser().getUserId())
                    .roomId(order.getRoom().getRoomId())
                    .requestId(Optional.ofNullable(order.getRoomRequest()).map(RoomRequest::getRequestId).orElse(null))
                    .arrivalDate(order.getArrivalDate())
                    .departureDate(order.getDepartureDate())
                    .orderDate(order.getOrderDate())
                    .status(order.getStatus())
                    .price(order.getPrice())
                    .build();
            roomOrderDTOList.add(roomOrderDTO);
        }
        return roomOrderDTOList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeRoomOrderById(int orderId, int userId) throws NotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User user = userOptional.get();
        if (userId == user.getUserId()) {
            roomOrderRepository.deleteById(orderId);

        }
    }

}
