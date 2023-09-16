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

    public WingitUser getById(String username) {
        return repo.findById(username).orElse(null);
    }

    @Transactional
    public WingitUser createUser(WingitUser newUser) {
        if (repo.existsById(newUser.getUsername())) throw new UserBadRequestException("Username already exists");
        if (repo.existsByEmail(newUser.getEmail())) throw new UserBadRequestException("Email already used for existing account.");
        return repo.save(newUser);
    }

    @Transactional
    public void deleteUserById(String username) {
        if (repo.existsById(username)) {
            repo.deleteById(username);
        } else {
            throw new UserNotFoundException(username);
        }
    }

    @Transactional
    public WingitUser updateUser(WingitUser updatedUser) {
        boolean userExists = repo.existsById(updatedUser.getUsername());
        if (!userExists) throw new UserNotFoundException(updatedUser.getUsername());
        return repo.save(updatedUser);
    }

    public Boolean verifyUserCredentials(String username, String password) {
        return repo.existsByUsernameAndPassword(username, password);
    }
}
