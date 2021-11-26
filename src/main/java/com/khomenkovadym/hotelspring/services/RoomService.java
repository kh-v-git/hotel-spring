package com.khomenkovadym.hotelspring.services;

import com.khomenkovadym.hotelspring.model.RoomDTO;
import com.khomenkovadym.hotelspring.repositories.RoomRepository;
import com.khomenkovadym.hotelspring.entities.Room;
import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RoomService {
    private RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional
    public Page<Room> getRooms(Pageable pageable, Integer number) {
        return roomRepository.findAll(pageable);
    }

    public List<Room> getRoomsByRoomIdNotNull() {
        return roomRepository.getRoomsByRoomIdNotNull();
    }

    public List<Room> findAllRoomsByBedSize(RoomBedSize bedSize) {
        return roomRepository.findAllByBedSizeIs(bedSize);
    }

    public List<RoomBedSize> getAllAvailableBedSized() {
        return roomRepository.findDistinctBedSize();
    }

    public Room getRoomByRoomID(int roomId) {return roomRepository.getRoomByRoomId(roomId);}

    public Integer findMaxChildCapacity() {
        return roomRepository.findMaxChildCapacity();
    }

    public Integer findMaxAdultCapacity() {
        return roomRepository.findMaxAdultCapacity();
    }


}
