package com.G2T5203.wingit.user;

import com.G2T5203.wingit.entities.WingitUser;
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
        return service.getById(userID);
    }

    // POST to add a new user
    // TODO: Need set the path properly.
    @PostMapping
    public WingitUser addUser(@RequestBody WingitUser newUser) {
        // TODO: Implement code to add a new user to the repository
        return null; // Example for JPA: userRepository.save(newUser)
    }

    // DELETE a specific user by userID
    @DeleteMapping(path = "/users/delete/{userID}")
    public void deleteUser(@PathVariable String userID) {
        // TODO: To be implemented
    }

    // PUT to update a specific user by userID
    @PutMapping("/users/update{userID}")
    public WingitUser updateUser(@PathVariable String userID, @RequestBody WingitUser updatedUser) {
        // TODO: Implement code to update an existing user in the repository
        return null; // Example for JPA: userRepository.save(updatedUser)
    }


}
