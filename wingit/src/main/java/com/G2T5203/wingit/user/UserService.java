package com.G2T5203.wingit.user;

import com.G2T5203.wingit.entities.WingitUser;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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

    @Transactional
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

    @Transactional
    public HttpStatus deleteUserById(Integer userId) {
        try {
            repo.deleteById(userId);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Failed to delete user by Id: EmptyResultDataAccessException\n" +
                    "UserId that was attempted to be deleted: " + userId);
            logger.debug("Error details: " + e.getLocalizedMessage());
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus updateUser(WingitUser updatedUser) {
        try {
            boolean userExists = getById(updatedUser.getUserId()) != null;
            if (userExists) {
                repo.save(updatedUser);
            } else {
                logger.error("User " + updatedUser.getUserId() + " does not exist!");
                return HttpStatus.BAD_REQUEST;
            }
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to add new User: DataIntegrityViolationException\n" + updatedUser.toString());
            logger.debug("Error details: " + e.getLocalizedMessage());
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.OK;
    }
}
