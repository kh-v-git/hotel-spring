package com.khomenkovadym.hotelspring.repositories;

import com.khomenkovadym.hotelspring.entities.RoomOrder;
import com.khomenkovadym.hotelspring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomOrderRepository extends JpaRepository<RoomOrder, Integer> {

   List<RoomOrder> findAllByUser(User user);
}
