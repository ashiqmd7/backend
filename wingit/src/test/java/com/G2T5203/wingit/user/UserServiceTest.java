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

    @Mock
    private BCryptPasswordEncoder encoder;

    private TestUtils testUtils = new TestUtils(-1, encoder);

    @Test
    void getAllUsers_Success() {
        List<WingitUser> users = new ArrayList<>();
        users.add(testUtils.createSampleUser1(false));

        when(userRepository.findAll()).thenReturn(users);

        List<WingitUser> result = userService.getAllUsers();

        assertEquals(users, result);
        verify(userRepository).findAll();
    }

    @Test
    void getById_UserExists_Success() {
        WingitUser user = testUtils.createSampleUser1(false);

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

    @Test
    void createNormalUser_Success() {
        WingitUser newUser = testUtils.createSampleUser1(false);
        newUser.setAuthorityRole("ROLE_ADMIN"); // We test that the authority is forced to ROLE_USER.

        when(userRepository.existsById(any(String.class))).thenReturn(false);
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(userRepository.save(any(WingitUser.class))).thenAnswer((i) -> i.getArguments()[0] );
        final String mockedHashedPassword = "MOCKED_HASED_PASSWORD";
        when(encoder.encode(newUser.getPassword())).thenReturn(mockedHashedPassword);

        WingitUser result = userService.createUser(newUser);
        assertNotNull(result);
        assertEquals("ROLE_USER", result.getAuthorityRole());
        assertEquals(mockedHashedPassword, result.getPassword());
        verify(userRepository).existsById(any(String.class));
        verify(userRepository).existsByEmail(any(String.class));
        verify(userRepository).save(any(WingitUser.class));
    }

    @Test
    void createNormalUser_UserIdExists_Failure() {
        when(userRepository.existsById(any(String.class))).thenReturn(true);
        WingitUser newUser = testUtils.createSampleUser1(false);

        UserBadRequestException exception = assertThrows(UserBadRequestException.class, () -> {
            userService.createUser(newUser);
        });

        verify(userRepository).existsById(any(String.class));
        assertEquals("BAD REQUEST: Username already exists", exception.getMessage());
    }

    @Test
    void createNormalUser_EmailExists_Failure() {
        when(userRepository.existsById(any(String.class))).thenReturn(false);
        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);
        WingitUser newUser = testUtils.createSampleUser1(false);

        UserBadRequestException exception = assertThrows(UserBadRequestException.class, () -> {
            userService.createUser(newUser);
        });

        verify(userRepository).existsById(any(String.class));
        verify(userRepository).existsByEmail(any(String.class));
        assertEquals("BAD REQUEST: Email already used for existing account.", exception.getMessage());
    }

    // TODO: Create Admin Success & Fail case


}

