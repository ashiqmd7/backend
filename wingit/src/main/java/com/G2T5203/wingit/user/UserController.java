package com.G2T5203.wingit.user;

import com.G2T5203.wingit.entities.WingitUser;
import jakarta.validation.Valid;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
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


    private boolean isAdmin(UserDetails userDetails) { return userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN")); }
    private boolean isAdmin(Jwt jwt) { return jwt.getClaim("role").equals("ROLE_ADMIN"); }
    private boolean isNeitherUserNorAdmin(String username, UserDetails userDetails) {
        boolean isUser = username.equals(userDetails.getUsername());
        boolean isAdmin = isAdmin(userDetails);

        return (!isUser && !isAdmin);
    }
    private boolean isNeitherUserNorAdmin(String username, Jwt jwt) {
        boolean isUser = username.equals(jwt.getClaim("sub"));
        boolean isAdmin = isAdmin(jwt);

        return (!isUser && !isAdmin);
    }
    private void checkIfNotUserNorAdmin(String username, UserDetails userDetails, Jwt jwt) {
        if (jwt != null) {
            if (isNeitherUserNorAdmin(username, jwt)) throw new UserBadRequestException("Not the same user.");
        } else if (userDetails != null) {
            if (isNeitherUserNorAdmin(username, userDetails)) throw new UserBadRequestException("Not the same user.");
        } else {
            throw new UserBadRequestException("No AuthenticationPrincipal provided for check.");
        }
    }

    // GET a specific user by username
    @GetMapping(path = "/users/{username}")
    public WingitUser getUser(@PathVariable String username, @AuthenticationPrincipal UserDetails userDetails, @AuthenticationPrincipal Jwt jwt) {
        checkIfNotUserNorAdmin(username, userDetails, jwt);
        WingitUser user = service.getById(username);
        if (user == null) throw new UserNotFoundException(username);
        return user;
    }

    @GetMapping(path = "/users/authTest")
    public void getAuthTest() {
        // Intentionally left empty. Just a status check.
    }


    @GetMapping(path = "/users/adminAuthTest")
    public void getAdminAuthTest(@AuthenticationPrincipal UserDetails userDetails, @AuthenticationPrincipal Jwt jwt) {
        if (userDetails != null) {
            if (!isAdmin(userDetails)) throw new UserBadRequestException("User is not admin.");
        } else if (jwt != null) {
            if (!isAdmin(jwt)) throw new UserBadRequestException("User is not admin.");
        } else {
            throw new UserBadRequestException("No Authentication passed to check.");
        }
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/users/newAdmin")
    public WingitUser createAdmin(@Valid @RequestBody WingitUser newAdmin) {
        try {
            return service.createAdmin(newAdmin);
        } catch (Exception e) {
            throw new UserBadRequestException(e);
        }
    }

    // DELETE a specific user by username
    @DeleteMapping(path = "/users/delete/{username}")
    public void deleteUser(@PathVariable String username, @AuthenticationPrincipal UserDetails userDetails, @AuthenticationPrincipal Jwt jwt) {
        checkIfNotUserNorAdmin(username, userDetails, jwt);
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
    public WingitUser updateUser(@PathVariable String username, @Valid @RequestBody WingitUser updatedUser,
                                 @AuthenticationPrincipal UserDetails userDetails, @AuthenticationPrincipal Jwt jwt) {
        checkIfNotUserNorAdmin(username, userDetails, jwt);

        boolean usernamesMatch = username.equals(updatedUser.getUsername());
        if (!usernamesMatch) throw new UserBadRequestException("Path username and payload username mismatch.");

        try {
            return service.updateUser(updatedUser);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UserBadRequestException(e);
        }
    }

    @PutMapping("/users/updatePass/{username}")
    public WingitUser updatePassword(@PathVariable String username, @RequestBody Map<String, Object> newPassword,
                                     @AuthenticationPrincipal UserDetails userDetails, @AuthenticationPrincipal Jwt jwt) {
        checkIfNotUserNorAdmin(username, userDetails, jwt);

        JSONObject jsonObj = new JSONObject(newPassword);
        try {
            String jsonPassword = jsonObj.getString("password");
            return service.updatePassword(username, jsonPassword);
        } catch (Exception e) {
            throw new UserBadRequestException(e);
        }
    }
}
