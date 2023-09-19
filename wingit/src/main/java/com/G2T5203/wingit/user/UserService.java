package com.G2T5203.wingit.user;

import com.G2T5203.wingit.entities.WingitUser;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository repo, BCryptPasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
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
        Optional<WingitUser> retrievedUser = repo.findById(updatedUser.getUsername());
        if (retrievedUser.isEmpty()) throw new UserNotFoundException(updatedUser.getUsername());

        // Enforce no changes to password and role
        updatedUser.setPassword(retrievedUser.get().getPassword());
        updatedUser.setAuthorityRole(retrievedUser.get().getAuthorityRole());
        return repo.save(updatedUser);
    }

    @Transactional
    public WingitUser updatePassword(String username, String newPassword) {
        Optional<WingitUser> retrievedUser = repo.findById(username);
        if (retrievedUser.isEmpty()) throw new UsernameNotFoundException(username);

        String newHashedPassword = encoder.encode(newPassword);
        retrievedUser.get().setPassword(newHashedPassword);
        return repo.save(retrievedUser.get());
    }
}
