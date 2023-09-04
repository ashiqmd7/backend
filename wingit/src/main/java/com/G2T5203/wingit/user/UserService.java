package com.G2T5203.wingit.user;

import com.G2T5203.wingit.entities.WingitUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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

    public HttpStatus createUser(WingitUser newUser) {
        try {
            repo.save(newUser);
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to add new User: DataIntegrityViolationException\n" + newUser.toString());
            logger.debug("Error details: " + e.getLocalizedMessage());
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.CREATED;
    }
}
