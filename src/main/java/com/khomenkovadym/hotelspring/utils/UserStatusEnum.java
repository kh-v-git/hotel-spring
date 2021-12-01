package com.khomenkovadym.hotelspring.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum UserStatusEnum {
    ACTIVE, DEACTIVATED;

    public static List<String> getUserStatusList () {
        return  Arrays.stream(UserStatusEnum.values())
                .map(UserStatusEnum::name)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
