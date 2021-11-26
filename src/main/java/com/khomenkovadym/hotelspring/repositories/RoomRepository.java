package com.khomenkovadym.hotelspring.repositories;

import com.khomenkovadym.hotelspring.entities.Room;
import com.khomenkovadym.hotelspring.utils.RoomBedSize;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
