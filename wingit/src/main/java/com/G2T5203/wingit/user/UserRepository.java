package com.G2T5203.wingit.user;

import com.G2T5203.wingit.entities.WingitUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<WingitUser, Integer> {

}
