package com.khomenkovadym.hotelspring.services;

import com.khomenkovadym.hotelspring.entities.Room;
import com.khomenkovadym.hotelspring.entities.RoomOrder;
import com.khomenkovadym.hotelspring.entities.RoomRequest;
import com.khomenkovadym.hotelspring.entities.User;
import com.khomenkovadym.hotelspring.model.RoomRequestDTO;
import com.khomenkovadym.hotelspring.model.UserRequestDTO;
import com.khomenkovadym.hotelspring.repositories.RoomOrderRepository;
import com.khomenkovadym.hotelspring.repositories.RoomRepository;
import com.khomenkovadym.hotelspring.repositories.RoomRequestRepository;
import com.khomenkovadym.hotelspring.repositories.UserRepository;
import com.khomenkovadym.hotelspring.utils.RoomOrderStatus;
import com.khomenkovadym.hotelspring.utils.RoomRequestStatus;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RoomRequestService {

    private final RoomRequestRepository roomRequestRepository;
    private final UserRepository userRepository;
    private final RoomOrderRepository roomOrderRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public RoomRequestService(RoomRequestRepository roomRequestRepository, UserRepository userRepository, RoomOrderRepository roomOrderRepository, RoomRepository roomRepository) {
        this.roomRequestRepository = roomRequestRepository;
        this.userRepository = userRepository;
        this.roomOrderRepository = roomOrderRepository;
        this.roomRepository = roomRepository;
    }

    public List<RoomRequestDTO> getRoomRequestListWithRoomNumberByUser(Integer userId) {
        return roomRequestRepository.getUserRequestsListWithRoomNumberFromOrderByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveRequest(UserRequestDTO dto, Integer userId) throws NotFoundException {

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        RoomRequest request = RoomRequest.builder()
                .arrivalDate(dto.getArrivalDate())
                .departureDate(dto.getDepartureDate())
                .adultsCapacity(dto.getAdultsCapacity())
                .childrenCapacity(dto.getChildrenCapacity())
                .bedSize(dto.getBedSize())
                .status(RoomRequestStatus.REQUESTED)
                .bedSize(dto.getBedSize())
                .user(userOptional.get())
                .build();

        roomRequestRepository.saveAndFlush(request);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeRequestByIdUserId(int requestId, int userId) throws NotFoundException {
        Optional<RoomRequest> request = roomRequestRepository.findById(requestId);

        if (request.isEmpty()) {
            throw new NotFoundException(String.format("Request not found ID='%d'", requestId));
        }
        RoomRequest roomRequest = request.get();
        if (userId != roomRequest.getUser().getUserId()) {
            throw new NotFoundException(String.format("User has no order with ID='%d'", userId));
        }
        roomRequestRepository.deleteById(requestId);
    }

    public List<RoomRequestDTO> getAllRoomRequestList() {
        return roomRequestRepository.getAllUserRequestsWithRoomNumber();
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeRequestById(Integer requestId) {
        roomRequestRepository.deleteById(requestId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void declineRequestById(Integer requestId) throws NotFoundException {
        Optional<RoomRequest> maybeRequest = roomRequestRepository.findById(requestId);
        if (maybeRequest.isEmpty()) {
            throw new NotFoundException(String.format("Request not found ID='%d'", requestId));
        }

        RoomRequest updateRequest = maybeRequest.get();
        updateRequest.setStatus(RoomRequestStatus.DECLINED);
        roomRequestRepository.saveAndFlush(updateRequest);
    }

    public UserRequestDTO findUserRequestDTOById(Integer requestId) throws NotFoundException {
        Optional<RoomRequest> maybeReq = roomRequestRepository.findById(requestId);
        if (maybeReq.isPresent()) {
            RoomRequest roomRequest = maybeReq.get();
            return UserRequestDTO.builder()
                    .adultsCapacity(roomRequest.getAdultsCapacity())
                    .childrenCapacity(roomRequest.getChildrenCapacity())
                    .bedSize(roomRequest.getBedSize())
                    .arrivalDate(roomRequest.getArrivalDate())
                    .departureDate(roomRequest.getDepartureDate())
                    .build();
        } else {
            throw new NotFoundException("Request not found by Id");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void assignRoomToRequestByManager(Integer requestId, Integer roomId, UserRequestDTO requestDto) throws NotFoundException {
        Optional<RoomRequest> maybeReq = roomRequestRepository.findById(requestId);
        Optional<Room> maybeRoom = roomRepository.findById(roomId);
        if (maybeReq.isPresent() && maybeRoom.isPresent()) {
            Optional<User> maybeUser = userRepository.findById(maybeReq.get().getUser().getUserId());
            Duration days = Duration.between(requestDto.getArrivalDate().atStartOfDay(), requestDto.getDepartureDate().atStartOfDay());
            long daysBetween = days.toDays();
            Double roomPrice = maybeRoom.get().getPrice();
            roomPrice *= daysBetween;
            Double bookingPrice = Math.round(roomPrice * 100.00) / 100.00;

            RoomRequest updateReq = maybeReq.get();
            updateReq.setStatus(RoomRequestStatus.ASSIGNED);
            RoomOrder assignOrder = RoomOrder.builder()
                    .user(maybeUser.get())
                    .room(maybeRoom.get())
                    .roomRequest(updateReq)
                    .status(RoomOrderStatus.OFFERED)
                    .orderDate(LocalDateTime.now())
                    .arrivalDate(requestDto.getArrivalDate())
                    .departureDate(requestDto.getDepartureDate())
                    .price(bookingPrice)
                    .build();
            roomOrderRepository.saveAndFlush(assignOrder);
        } else {
            throw new NotFoundException("Request not assigned by Id");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void declineUserRequestById(Integer requestId, Integer userId) throws NotFoundException {
        Optional<RoomRequest> maybeRequest = roomRequestRepository.findById(requestId);
        if (maybeRequest.isEmpty()) {
            throw new NotFoundException(String.format("Request not found ID='%d'", requestId));
        }
        RoomRequest updateRequest = maybeRequest.get();
        if (updateRequest.getUser().getUserId() == userId) {
            updateRequest.setStatus(RoomRequestStatus.DECLINED);
            roomRequestRepository.saveAndFlush(updateRequest);
        } else {
            throw new NotFoundException("User has no such request");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void approveUserRequestById(Integer requestId, Integer userId) throws NotFoundException {
        Optional<RoomRequest> maybeRequest = roomRequestRepository.findById(requestId);
        if (maybeRequest.isEmpty()) {
            throw new NotFoundException(String.format("Request not found ID='%d'", requestId));
        }
        RoomRequest updateRequest = maybeRequest.get();
        if (updateRequest.getUser().getUserId() == userId) {
            RoomOrder updateRoomOrder = roomOrderRepository.findRoomOrderByRoomRequest(updateRequest);
            updateRoomOrder.setStatus(RoomOrderStatus.PENDING_PAYMENT);
            roomOrderRepository.saveAndFlush(updateRoomOrder);
            updateRequest.setStatus(RoomRequestStatus.APPROVED);
            roomRequestRepository.saveAndFlush(updateRequest);
        } else {
            throw new NotFoundException("User has no such request");
        }

    }
}
