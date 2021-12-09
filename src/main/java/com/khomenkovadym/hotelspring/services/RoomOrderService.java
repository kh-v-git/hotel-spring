package com.khomenkovadym.hotelspring.services;

import com.khomenkovadym.hotelspring.entities.Room;
import com.khomenkovadym.hotelspring.entities.RoomOrder;
import com.khomenkovadym.hotelspring.entities.RoomRequest;
import com.khomenkovadym.hotelspring.entities.User;
import com.khomenkovadym.hotelspring.model.RoomOrderDTO;
import com.khomenkovadym.hotelspring.model.UserRequestDTO;
import com.khomenkovadym.hotelspring.repositories.RoomOrderRepository;
import com.khomenkovadym.hotelspring.repositories.RoomRepository;
import com.khomenkovadym.hotelspring.repositories.UserRepository;
import com.khomenkovadym.hotelspring.utils.RoomOrderStatus;
import com.mysql.cj.exceptions.WrongArgumentException;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomOrderService {
    private final RoomOrderRepository roomOrderRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public RoomOrderService(RoomOrderRepository roomOrderRepository, UserRepository userRepository, RoomRepository roomRepository) {
        this.userRepository = userRepository;
        this.roomOrderRepository = roomOrderRepository;
        this.roomRepository = roomRepository;
    }



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
    public void removeRoomOrderByUserById(int orderId, int userId) throws NotFoundException {
        Optional<RoomOrder> orderOptional = roomOrderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            throw new NotFoundException (String.format("Order not found ID='%d'", orderId));
        }
        RoomOrder roomOrder = orderOptional.get();
        if (userId != roomOrder.getUser().getUserId()) {
            throw new NotFoundException (String.format("User has no order with ID='%d'", userId));
        }
        roomOrderRepository.deleteById(orderId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void declineRoomOrderById(int orderId, int userId) throws NotFoundException {
        Optional<RoomOrder> orderOptional = roomOrderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            throw new NotFoundException (String.format("Order not found ID='%d'", orderId));
        }
        RoomOrder roomOrder = orderOptional.get();
        if (userId != roomOrder.getUser().getUserId()) {
            throw new NotFoundException (String.format("User has no order with ID='%d'", userId));
        }
        RoomOrder updateOrder = RoomOrder.builder()
                .orderId(roomOrder.getOrderId())
                .user(roomOrder.getUser())
                .room(roomOrder.getRoom())
                .roomRequest(roomOrder.getRoomRequest())
                .status(RoomOrderStatus.EXPIRED)
                .orderDate(roomOrder.getOrderDate())
                .arrivalDate(roomOrder.getArrivalDate())
                .departureDate(roomOrder.getDepartureDate())
                .price(roomOrder.getPrice())
                .build();
        roomOrderRepository.saveAndFlush(updateOrder);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addRoomOrder(int userId, int roomId, UserRequestDTO reqDTO) throws NotFoundException {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new NotFoundException("User by id not found");
        }
        Optional<Room> maybeRoom = roomRepository.findById(roomId);
        if (maybeRoom.isEmpty()) {
            throw new NotFoundException("Room by id not found");
        }
        Duration days = Duration.between(reqDTO.getArrivalDate().atStartOfDay(), reqDTO.getDepartureDate().atStartOfDay());
        long daysBetween = days.toDays();
        Double roomPrice = maybeRoom.get().getPrice();
        roomPrice *= daysBetween;
        Double bookingPrice = Math.round(roomPrice * 100.00) / 100.00;

        RoomOrder newOrder = RoomOrder.builder()
                .room(maybeRoom.get())
                .user(maybeUser.get())
                .arrivalDate(reqDTO.getArrivalDate())
                .departureDate(reqDTO.getDepartureDate())
                .orderDate(LocalDateTime.now())
                .status(RoomOrderStatus.PENDING_PAYMENT)
                .price(bookingPrice)
                .roomRequest(null)
                .build();
        roomOrderRepository.saveAndFlush(newOrder);
    }

    public List<RoomOrderDTO> findActiveUserOrders (RoomOrderStatus status) throws NotFoundException {
        List<RoomOrder> roomOrderList = roomOrderRepository.findAllActiveOrders(status);
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
    public void removeRoomOrderById(int orderId) {
        roomOrderRepository.deleteById(orderId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void lockRoomByManager(Integer userId, Integer roomId, UserRequestDTO reqDto) throws NotFoundException {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new NotFoundException("User by id not found");
        }
        Optional<Room> maybeRoom = roomRepository.findById(roomId);
        if (maybeRoom.isEmpty()) {
            throw new NotFoundException("Room by id not found");
        }
        Duration days = Duration.between(reqDto.getArrivalDate().atStartOfDay(), reqDto.getDepartureDate().atStartOfDay());
        long daysBetween = days.toDays();
        Double roomPrice = maybeRoom.get().getPrice();
        roomPrice *= daysBetween;
        Double bookingPrice = Math.round(roomPrice * 100.00) / 100.00;

        RoomOrder newOrder = RoomOrder.builder()
                .room(maybeRoom.get())
                .user(maybeUser.get())
                .arrivalDate(reqDto.getArrivalDate())
                .departureDate(reqDto.getDepartureDate())
                .orderDate(LocalDateTime.now())
                .status(RoomOrderStatus.INACCESSIBLE)
                .price(bookingPrice)
                .roomRequest(null)
                .build();
        roomOrderRepository.saveAndFlush(newOrder);
    }
}
