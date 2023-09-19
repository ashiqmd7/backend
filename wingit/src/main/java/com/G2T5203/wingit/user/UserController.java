package com.G2T5203.wingit.user;

import com.G2T5203.wingit.entities.WingitUser;
import jakarta.validation.Valid;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service = service; }

    // GET all users
    @GetMapping(path = "/users")
    public List<WingitUser> getAllUsers() {
        return service.getAllUsers();
    }


    private boolean isUserOrAdmin(String username, UserDetails userDetails) {
        boolean isUser = username.equals(userDetails.getUsername());
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        return (isUser || isAdmin);
    }

    // GET a specific user by username
    @GetMapping(path = "/users/{username}")
    public WingitUser getUser(@PathVariable String username, @AuthenticationPrincipal UserDetails userDetails) {
        if (!isUserOrAdmin(username, userDetails)) throw new UserBadRequestException("Not the same user.");

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
            return service.createUser(newUser);
        } catch (Exception e) {
            throw new UserBadRequestException(e);
        }
    }

    // DELETE a specific user by username
    @DeleteMapping(path = "/users/delete/{username}")
    public void deleteUser(@PathVariable String username, @AuthenticationPrincipal UserDetails userDetails) {
        if (!isUserOrAdmin(username, userDetails)) throw new UserBadRequestException("Not the same user.");

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
    public WingitUser updateUser(@PathVariable String username, @Valid @RequestBody WingitUser updatedUser, @AuthenticationPrincipal UserDetails userDetails) {
        if (!isUserOrAdmin(username, userDetails)) throw new UserBadRequestException("Not the same user.");

        boolean usernamesMatch = username.equals(updatedUser.getUsername());
        if (!usernamesMatch) throw new UserBadRequestException("Path username and payload username mismtach.");

        try {
            return service.updateUser(updatedUser);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UserBadRequestException(e);
        }
    }

    @PutMapping("/users/updatePass/{username}")
    public WingitUser updatePassword(@PathVariable String username, @RequestBody Map<String, Object> newPassword, @AuthenticationPrincipal UserDetails userDetails) {
        if (!isUserOrAdmin(username, userDetails)) throw new UserBadRequestException("Not the same user.");

        JSONObject jsonObj = new JSONObject(newPassword);
        try {
            String jsonPassword = jsonObj.getString("password");
            return service.updatePassword(username, jsonPassword);
        } catch (Exception e) {
            throw new UserBadRequestException(e);
        }
    }
}
