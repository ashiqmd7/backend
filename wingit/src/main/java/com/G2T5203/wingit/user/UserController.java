package com.G2T5203.wingit.user;

import com.G2T5203.wingit.entities.WingitUser;
import jakarta.validation.Valid;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserController {
    private final UserService service;
    private final BCryptPasswordEncoder encoder;
    public UserController(UserService service, BCryptPasswordEncoder encoder) {
        this.service = service;
        this.encoder = encoder;
    }

    // GET all users
    @GetMapping(path = "/users")
    public List<WingitUser> getAllUsers() {
        return service.getAllUsers();
    }

    // GET a specific user by username
    @GetMapping(path = "/users/{username}")
    public WingitUser getUser(@PathVariable String username) {
        // TODO: Make sure authenticated user is either admin role, or the exact same user.
        WingitUser user = service.getById(username);
        if (user == null) throw new UserNotFoundException(username);
        return user;
    }

    @GetMapping(path = "/users/authTest/{var}")
    public ResponseEntity<Object> getAuthTest(@PathVariable String var) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("pathInput", var);
        return new ResponseEntity<>(body, null, HttpStatus.OK);
    }

    // POST to add a new user
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/users/new")
    public WingitUser createUser(@Valid @RequestBody WingitUser newUser) {
        try {
            if (newUser.getAuthorityRole() == null) {
                newUser.setAuthorityRole("ROLE_USER");
            }
            newUser.setPassword(encoder.encode(newUser.getPassword()));
            return service.createUser(newUser);
        } catch (Exception e) {
            throw new UserBadRequestException(e);
        }
    }

    // DELETE a specific user by username
    @DeleteMapping(path = "/users/delete/{username}")
    public void deleteUser(@PathVariable String username) {
        // TODO: Make sure authenticated user is either admin role, or the exact same user.
        // TODO: Then make sure it's in the unit test.
        try {
            service.deleteUserById(username);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UserBadRequestException(e);
        }
    }

    // PUT to update a specific user by username
    @PutMapping("/users/update/{username}")
    public WingitUser updateUser(@PathVariable String username, @Valid @RequestBody WingitUser updatedUser) {
        // TODO: Make sure authenticated user is either admin role, or the exact same user.
        boolean usernamesMatch = username.equals(updatedUser.getUsername());
        if (!usernamesMatch) throw new UserBadRequestException("Path username and payload username mismtach.");

        if (updatedUser.getAuthorityRole() == null) {
            updatedUser.setAuthorityRole("ROLE_USER");
        }
        try {
            // TODO: These logic should be moved to Service.
            WingitUser retrievedUser = service.getById(updatedUser.getUsername());
            updatedUser.setPassword(retrievedUser.getPassword()); // So the hash doesn't change in this update.
            updatedUser.setAuthorityRole(retrievedUser.getAuthorityRole()); // Ensures role remains the same.
            return service.updateUser(updatedUser);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UserBadRequestException(e);
        }
    }

    @PutMapping("/users/updatePass/{username}")
    public WingitUser updatePassword(@PathVariable String username, @RequestBody Map<String, Object> newPassword) {
        // TODO: Make sure authenticated user is either admin role, or the exact same user.
        JSONObject jsonObj = new JSONObject(newPassword);
        try {
            // TODO: These logic should be moved to service.
            String jsonPassword = jsonObj.getString("password");
            String hashedPassword = encoder.encode(jsonPassword);

            WingitUser retrievedUser = service.getById(username);
            if (retrievedUser == null) throw new UserNotFoundException(username);

            retrievedUser.setPassword(hashedPassword);
            return service.updateUser(retrievedUser);
        } catch (Exception e) {
            throw new UserBadRequestException(e);
        }
    }
}
