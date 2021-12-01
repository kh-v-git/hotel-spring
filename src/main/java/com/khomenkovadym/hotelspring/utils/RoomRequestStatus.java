package com.khomenkovadym.hotelspring.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum RoomRequestStatus {
    REQUESTED,
    ASSIGNED,
    APPROVED,
    DECLINED,
    EXPIRED;

    public static List<String> getRoomRequestStatusList () {
        return  Arrays.stream(RoomRequestStatus.values())
                .map(RoomRequestStatus::name)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
