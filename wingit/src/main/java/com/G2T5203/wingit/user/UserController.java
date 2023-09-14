package com.G2T5203.wingit.user;

import com.G2T5203.wingit.entities.WingitUser;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // GET all users
    @GetMapping(path = "/users")
    public List<WingitUser> getAllUsers() {
        return service.getAllUsers();
    }

    // GET a specific user by userID
    @GetMapping(path = "/users/{userID}")
    public WingitUser getUser(@PathVariable Integer userID) {
        WingitUser user = service.getById(userID);
        if (user == null) throw new UserNotFoundException(userID);
        return user;
    }

    // POST to add a new user
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/users/new")
    public WingitUser createUser(@Valid @RequestBody WingitUser newUser) {
        try {
            return service.createUser(newUser);
        } catch (Exception e) {
            throw new UserBadRequestException(e);
        }
    }

    // DELETE a specific user by userID
    @DeleteMapping(path = "/users/delete/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        try {
            service.deleteUserById(userId);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UserBadRequestException(e);
        }
    }

    // PUT to update a specific user by userID
    @PutMapping("/users/update/{userId}")
    public WingitUser updateUser(@PathVariable Integer userId, @Valid @RequestBody WingitUser updatedUser) {
        updatedUser.setUserId(userId);
        try {
            return service.updateUser(updatedUser);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UserBadRequestException(e);
        }
    }
}
