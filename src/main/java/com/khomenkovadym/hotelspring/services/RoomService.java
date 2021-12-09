package com.khomenkovadym.hotelspring.services;

import com.khomenkovadym.hotelspring.model.RoomDTO;
import com.khomenkovadym.hotelspring.model.UserRequestDTO;
import com.khomenkovadym.hotelspring.repositories.RoomRepository;
import com.khomenkovadym.hotelspring.entities.Room;
import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import com.khomenkovadym.hotelspring.utils.RoomOrderStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RoomService {
    private final ModelMapper modelMapper = new ModelMapper();

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

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

    public Room getRoomByRoomID(int roomId) {
        return roomRepository.getRoomByRoomId(roomId);
    }

    public Integer findMaxChildCapacity() {
        return roomRepository.findMaxChildCapacity();
    }

    public Integer findMaxAdultCapacity() {
        return roomRepository.findMaxAdultCapacity();
    }

    public List<RoomDTO> findFreeRoomsForRequest(UserRequestDTO requestDTO) {
        List<Room> freeRoomList = roomRepository.findAllFreeRooms(RoomOrderStatus.OFFERED, RoomOrderStatus.BOOKED, RoomOrderStatus.PENDING_PAYMENT, RoomOrderStatus.INACCESSIBLE,
                requestDTO.getArrivalDate(), requestDTO.getDepartureDate(),
                requestDTO.getAdultsCapacity(), requestDTO.getChildrenCapacity(), requestDTO.getBedSize());
        List<RoomDTO> freeRoomDTOList = new ArrayList<>();
        for (Room room : freeRoomList) {
            RoomDTO roomDTO = new RoomDTO();
            modelMapper.map(room, roomDTO);
            freeRoomDTOList.add(roomDTO);
        }
        return freeRoomDTOList;
    }


    public List<RoomDTO> findAllRooms(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Room> pageResult = roomRepository.findAll(paging);
        if(pageResult.hasContent()) {
            List<Room> roomList = pageResult.getContent();
            List<RoomDTO> roomDTOList = new ArrayList<>();
            for (Room room : roomList) {
                RoomDTO roomDTO = new RoomDTO();
                modelMapper.map(room, roomDTO);
                roomDTOList.add(roomDTO);
            }
            return roomDTOList;
        } else return new ArrayList<>();

    }

    public long getRoomAmount() {
        return roomRepository.count();
    }

    public void createRoom(RoomDTO roomDTO) {
        Room createRoom = Room.builder()
                .number(roomDTO.getNumber())
                .bedSize(roomDTO.getBedSize())
                .adultCapacity(roomDTO.getAdultCapacity())
                .childCapacity(roomDTO.getChildCapacity())
                .about(roomDTO.getAbout())
                .price(roomDTO.getPrice())
                .build();
        Room maybeRoom = roomRepository.saveAndFlush(createRoom);
    }
}
