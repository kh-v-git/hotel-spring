package com.khomenkovadym.hotelspring.services;

import com.khomenkovadym.hotelspring.entities.User;
import com.khomenkovadym.hotelspring.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    private UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Page<User> getUsers (Pageable pageable, Integer number) {
        return userRepository.findAll(pageable);
    }


}
