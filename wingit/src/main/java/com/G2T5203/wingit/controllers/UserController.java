package com.G2T5203.wingit.controllers;

import com.G2T5203.wingit.entities.WingitUser;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class UserController {

    // GET all users
    @GetMapping(path = "/users")
    public List<WingitUser> getAllUsers() {
        // TODO: To be implemented
        return null;
    }

    // GET a specific user by userID
    @GetMapping(path = "/users/{userID}")
    public WingitUser getUser(@PathVariable String userID) {
        // TODO: To be implemented
        return null;
    }

    // POST to add a new user
    @PostMapping
    public WingitUser addUser(@RequestBody WingitUser newUser) {
        // TODO: Implement code to add a new user to the repository
        return null; // Example for JPA: userRepository.save(newUser)
    }

    // DELETE a specific user by userID
    @DeleteMapping(path = "/users/{userID}")
    public void deleteUser(@PathVariable String userID) {
        // TODO: To be implemented
    }

    // PUT to update a specific user by userID
    @PutMapping("/users/{userID}")
    public WingitUser updateUser(@PathVariable String userID, @RequestBody WingitUser updatedUser) {
        // TODO: Implement code to update an existing user in the repository
        return null; // Example for JPA: userRepository.save(updatedUser)
    }


}
