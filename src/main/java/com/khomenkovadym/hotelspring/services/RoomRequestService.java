package com.khomenkovadym.hotelspring.services;

import com.khomenkovadym.hotelspring.entities.RoomRequest;
import com.khomenkovadym.hotelspring.entities.User;
import com.khomenkovadym.hotelspring.model.RoomRequestDTO;
import com.khomenkovadym.hotelspring.model.UserRequestDTO;
import com.khomenkovadym.hotelspring.repositories.RoomRequestRepository;
import com.khomenkovadym.hotelspring.repositories.UserRepository;
import com.khomenkovadym.hotelspring.utils.RoomRequestStatus;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoomRequestService {

    private RoomRequestRepository roomRequestRepository;
    private UserRepository userRepository;

    @Autowired
    public RoomRequestService(RoomRequestRepository roomRequestRepository, UserRepository userRepository) {
        this.roomRequestRepository = roomRequestRepository;
        this.userRepository = userRepository;
    }

    @Transactional
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
    public void removeRequestById(int requestId, int userId) throws NotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User user = userOptional.get();
        if (userId == user.getUserId()) {
            roomRequestRepository.deleteById(requestId);

        }
    }
}
