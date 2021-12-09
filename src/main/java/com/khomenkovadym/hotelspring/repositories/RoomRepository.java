package com.khomenkovadym.hotelspring.repositories;

import com.khomenkovadym.hotelspring.entities.Room;
import com.khomenkovadym.hotelspring.utils.RoomBedSize;

import com.khomenkovadym.hotelspring.utils.RoomOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> getRoomsByRoomIdNotNull();

    List<Room> findAllByBedSizeIs (RoomBedSize bedSize);

    @Query("SELECT DISTINCT r.bedSize FROM Room r")
    List<RoomBedSize> findDistinctBedSize();

    Room getRoomByRoomId(int id);

    @Query("SELECT MAX(r.adultCapacity) FROM Room r")
    Integer findMaxAdultCapacity();

    @Query("SELECT MAX(r.childCapacity) FROM Room r")
    Integer findMaxChildCapacity();

    @Query("SELECT rm FROM Room rm WHERE rm.roomId NOT IN (SELECT DISTINCT ro.room.roomId FROM RoomOrder ro WHERE (ro.status = :offered OR ro.status = :booked OR ro.status = :pending OR ro.status = :inaccessible) AND ((ro.arrivalDate <= :arrival AND ro.departureDate >= :arrival) OR (ro.arrivalDate <= :departure AND ro.departureDate >= :departure) OR (ro.arrivalDate >= :arrival AND ro.departureDate <= :departure))) AND rm.adultCapacity >= :adults AND rm.childCapacity >= :children and rm.bedSize = :bedSize")
    List<Room> findAllFreeRooms(RoomOrderStatus offered, RoomOrderStatus booked, RoomOrderStatus pending, RoomOrderStatus inaccessible,
                                LocalDate arrival, LocalDate departure,
                                int adults, int children, RoomBedSize bedSize);

}
