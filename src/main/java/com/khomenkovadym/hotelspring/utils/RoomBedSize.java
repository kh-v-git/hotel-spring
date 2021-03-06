package com.khomenkovadym.hotelspring.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum RoomBedSize {
    KING,
    QUEEN,
    TWIN,
    DOUBLE,
    COT;

    public static List<String> RoomBedSizeList () {
        return  Arrays.stream(RoomBedSize.values())
                .map(RoomBedSize::name)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

}
