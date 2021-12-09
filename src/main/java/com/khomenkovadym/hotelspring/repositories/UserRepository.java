package com.khomenkovadym.hotelspring.repositories;

import com.khomenkovadym.hotelspring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    List<User> findAllByEmailContains(String email);
}
