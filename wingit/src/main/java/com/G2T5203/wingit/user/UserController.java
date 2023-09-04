package com.G2T5203.wingit.user;

import com.G2T5203.wingit.entities.WingitUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.logging.Level;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
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
        return service.getById(userID);
    }

    // POST to add a new user
    @PostMapping(path = "/users/new")
    public ResponseEntity<String> createUser(@RequestBody WingitUser newUser) {
        logger.debug("RequestBody JSON: " + newUser.toString());
        HttpStatus resultingStatus = service.createUser(newUser);
        return ResponseEntity.status(resultingStatus).build();
    }

    // DELETE a specific user by userID
    @DeleteMapping(path = "/users/delete/{userID}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userID) {
        HttpStatus resultingStatus = service.deleteUserById(userID);
        return ResponseEntity.status(resultingStatus).build();
    }

    // PUT to update a specific user by userID
    @PutMapping("/users/update/{userID}")
    public ResponseEntity<String> updateUser(@PathVariable Integer userID, @RequestBody WingitUser updatedUser) {
        updatedUser.setUserId(userID);
        HttpStatus resultingStatus;
        resultingStatus = service.updateUser(updatedUser);

        return ResponseEntity.status(resultingStatus).build();
    }


}
