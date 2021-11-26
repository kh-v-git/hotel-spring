package com.khomenkovadym.hotelspring.services;

import com.khomenkovadym.hotelspring.entities.RoomOrder;
import com.khomenkovadym.hotelspring.repositories.RoomOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RoomOrderService {
    private RoomOrderRepository roomOrderRepository;

    public RoomOrderService(RoomOrderRepository roomOrderRepository) {
        this.roomOrderRepository = roomOrderRepository;
    }

    @Transactional
    public Page<RoomOrder> getRoomOrders(Pageable pageable, Integer number) {
        return roomOrderRepository.findAll(pageable);
    }
}
