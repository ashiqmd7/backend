package com.G2T5203.wingit.user;

import com.G2T5203.wingit.entities.WingitUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<WingitUser> getAllUsers() {
        return repo.findAll();
    }

    public WingitUser getById(Integer userID) {
        return repo.findById(userID).orElse(null);
    }
}
