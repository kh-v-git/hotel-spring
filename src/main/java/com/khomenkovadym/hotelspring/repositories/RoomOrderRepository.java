package com.khomenkovadym.hotelspring.repositories;

import com.khomenkovadym.hotelspring.entities.RoomOrder;
import com.khomenkovadym.hotelspring.entities.RoomRequest;
import com.khomenkovadym.hotelspring.entities.User;
import com.khomenkovadym.hotelspring.utils.RoomOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomOrderRepository extends JpaRepository<RoomOrder, Integer> {

   List<RoomOrder> findAllByUser(User user);

   @Query("SELECT ro FROM RoomOrder ro WHERE ro.status <> :status")
   List<RoomOrder> findAllActiveOrders(RoomOrderStatus status);

   @Query("SELECT ro FROM RoomOrder ro WHERE ro.roomRequest = :request")
   RoomOrder findRoomOrderByRoomRequest(RoomRequest request);
}
