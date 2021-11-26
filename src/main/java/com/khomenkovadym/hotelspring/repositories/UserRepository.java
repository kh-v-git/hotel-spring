package com.khomenkovadym.hotelspring.repositories;

import com.khomenkovadym.hotelspring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
