package com.khomenkovadym.hotelspring.services;

import com.khomenkovadym.hotelspring.entities.RoomRequest;
import com.khomenkovadym.hotelspring.repositories.RoomRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RoomRequestService {
    private RoomRequestRepository roomRequestRepository;

    public RoomRequestService(RoomRequestRepository roomRequestRepository) {
        this.roomRequestRepository = roomRequestRepository;
    }

    @Transactional
    public Page<RoomRequest> getRoomRequests(Pageable pageable, Integer number) {
        return roomRequestRepository.findAll(pageable);
    }
}
