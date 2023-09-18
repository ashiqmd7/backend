package com.G2T5203.wingit.user;

import com.G2T5203.wingit.entities.WingitUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<WingitUser, String> {
    WingitUser findByEmail(String email);
    Optional<WingitUser> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsernameAndPassword(String username, String password);
}
