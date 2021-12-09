package com.khomenkovadym.hotelspring.repositories;

import com.khomenkovadym.hotelspring.entities.RoomRequest;
import com.khomenkovadym.hotelspring.model.RoomRequestDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRequestRepository extends JpaRepository<RoomRequest, Integer> {


   @Query("SELECT new com.khomenkovadym.hotelspring.model.RoomRequestDTO(request.requestId, rOrder.room.roomId, request.user.userId, request.status, request.bedSize, request.adultsCapacity, request.childrenCapacity, request.arrivalDate, request.departureDate) FROM RoomRequest request LEFT OUTER JOIN RoomOrder rOrder on request.requestId = rOrder.roomRequest.requestId WHERE request.user.userId = :id")
   List<RoomRequestDTO> getUserRequestsListWithRoomNumberFromOrderByUserId(Integer id);

   @Query("SELECT new com.khomenkovadym.hotelspring.model.RoomRequestDTO(request.requestId, rOrder.room.roomId, request.user.userId, request.status, request.bedSize, request.adultsCapacity, request.childrenCapacity, request.arrivalDate, request.departureDate) FROM RoomRequest request LEFT OUTER JOIN RoomOrder rOrder on request.requestId = rOrder.roomRequest.requestId")
   List<RoomRequestDTO> getAllUserRequestsWithRoomNumber();

}
