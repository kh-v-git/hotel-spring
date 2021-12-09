package com.khomenkovadym.hotelspring.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum RoomOrderStatus {
    FREE,
    OFFERED,
    PENDING_PAYMENT,
    BOOKED,
    EXPIRED,
    INACCESSIBLE;

    public static List<String> getRoomOrderStatusList () {
        return  Arrays.stream(RoomOrderStatus.values())
                .map(RoomOrderStatus::name)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
