package com.khomenkovadym.hotelspring.services;

import com.khomenkovadym.hotelspring.entities.User;
import com.khomenkovadym.hotelspring.model.CustomUserDetails;
import com.khomenkovadym.hotelspring.model.UserRegistrationDTO;
import com.khomenkovadym.hotelspring.repositories.UserRepository;
import com.khomenkovadym.hotelspring.utils.UserRoleEnum;
import com.khomenkovadym.hotelspring.utils.UserStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {


    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Transactional
    public Page<User> getUsers(Pageable pageable, Integer number) {
        return userRepository.findAll(pageable);
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(s);
        if (userOptional.isEmpty()) {
            return null;
        }
        return new CustomUserDetails(userOptional.get());
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAndFlush(UserRegistrationDTO userDTO) {
        User user = User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .phone(userDTO.getPhone())
                .email(userDTO.getEmail())
                .role(UserRoleEnum.USER)
                .status(UserStatusEnum.ACTIVE)
                .about(userDTO.getAbout())
                .password(passwordEncoder().encode(userDTO.getPassword()))
                .build();
        userRepository.saveAndFlush(user);
    }

}
