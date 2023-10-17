package com.G2T5203.wingit.user;

import com.G2T5203.wingit.TestUtils;
import com.G2T5203.wingit.plane.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private TestUtils testUtils = new TestUtils(-1, new BCryptPasswordEncoder());

    @Test
    void getAllUsers_Success() {
        List<WingitUser> users = new ArrayList<>();
        users.add(testUtils.createSampleUser1());

        when(userRepository.findAll()).thenReturn(users);

        List<WingitUser> result = userService.getAllUsers();

        assertEquals(users, result);
        verify(userRepository).findAll();
    }

    @Test
    void getById_UserExists_Success() {
        WingitUser user = testUtils.createSampleUser1();

        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));

        WingitUser result = userService.getById(user.getUsername());
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository).findById(user.getUsername());
    }

    @Test
    void getById_PlaneNotFound_Failure() {
        String nonExistentUsername = "NonExistentUsername";
        when(userRepository.findById(any(String.class))).thenReturn(Optional.empty());

        WingitUser result = userService.getById(nonExistentUsername);

        assertNull(result);
        verify(userRepository).findById(nonExistentUsername);
    }

}

