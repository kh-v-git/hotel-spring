package com.khomenkovadym.hotelspring.services;

import com.khomenkovadym.hotelspring.entities.User;
import com.khomenkovadym.hotelspring.model.CustomUserDetails;
import com.khomenkovadym.hotelspring.model.UserDTO;
import com.khomenkovadym.hotelspring.model.UserRegistrationDTO;
import com.khomenkovadym.hotelspring.repositories.UserRepository;
import com.khomenkovadym.hotelspring.utils.UserRoleEnum;
import com.khomenkovadym.hotelspring.utils.UserStatusEnum;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final ModelMapper modelMapper = new ModelMapper();

    private final UserRepository userRepository;

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

    public UserDTO findUserById(int userId) throws NotFoundException {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new NotFoundException("No search user in DB");
        }
        User user = maybeUser.get();
        return UserDTO.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .about(user.getAbout())
                .build();
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

    @Transactional(rollbackFor = Exception.class)
    public void updateUserById(int userId, UserDTO userDTO) throws NotFoundException {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new NotFoundException("No search user in DB");
        }
        User user = maybeUser.get();
        User saveUser = User.builder()
                .userId(user.getUserId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .password(user.getPassword())
                .role(UserRoleEnum.USER)
                .status(UserStatusEnum.ACTIVE)
                .about(userDTO.getAbout())
                .build();
        userRepository.saveAndFlush(saveUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserAdminById(int userId, UserDTO userDTO) throws NotFoundException {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new NotFoundException("No search user in DB");
        }
        User user = maybeUser.get();
        User saveUser = User.builder()
                .userId(user.getUserId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .password(user.getPassword())
                .role(userDTO.getRole())
                .status(userDTO.getStatus())
                .about(userDTO.getAbout())
                .build();
        userRepository.saveAndFlush(saveUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUserById(int userId) throws NotFoundException {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new NotFoundException("No such user in DB");
        }
        User deleteUser = maybeUser.get();
        userRepository.delete(deleteUser);
    }

    public List<UserDTO> findAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for(User user:userList) {
            UserDTO userDTO = new UserDTO();
            modelMapper.map(user, userDTO );
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    public List<UserDTO> findAllByEmailLike(String email) {
        List<User> userList = userRepository.findAllByEmailContains(email);
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user:userList) {
            UserDTO userDTO = new UserDTO();
            modelMapper.map(user, userDTO);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }
}
