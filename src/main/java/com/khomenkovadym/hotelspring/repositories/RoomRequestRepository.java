package com.khomenkovadym.hotelspring.repositories;

import com.khomenkovadym.hotelspring.entities.RoomRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRequestRepository extends JpaRepository<RoomRequest, Integer> {
    List<RoomRequest> getRoomRequestByRequestIdNotNull();
}
