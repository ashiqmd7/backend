package com.G2T5203.wingit.user;

import com.G2T5203.wingit.entities.WingitUser;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<WingitUser> getAllUsers() {
        return repo.findAll();
    }

    public WingitUser getById(Integer userID) {
        return repo.findById(userID).orElse(null);
    }

    @Transactional
    public WingitUser createUser(WingitUser newUser) { return repo.save(newUser); }

    @Transactional
    public void deleteUserById(Integer userId) {
        if (repo.existsById(userId)) {
            repo.deleteById(userId);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Transactional
    public WingitUser updateUser(WingitUser updatedUser) {
        boolean userExists = repo.existsById(updatedUser.getUserId());
        if (!userExists) throw new UserNotFoundException(updatedUser.getUserId());
        return repo.save(updatedUser);
    }
}
