package com.G2T5203.wingit.user;

import com.G2T5203.wingit.entities.WingitUser;
import com.mysql.cj.log.Log;
import org.apache.coyote.Response;
import org.glassfish.jaxb.runtime.v2.runtime.reflect.opt.Const;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @LocalServerPort
    private int port;
    private final String baseUrl = "http://localhost:";
    private URI constructUri(String path) throws URISyntaxException {
        return new URI(baseUrl + port + "/" + path);
    }
    // Helper functions
    private String startingUrl() { return baseUrl + port + "/"; }
    private WingitUser createSampleUser1() {
        return new WingitUser(
                "goodpassword",
                "Brandon",
                "Choy",
                Date.valueOf(LocalDate.parse("2001-12-04")),
                "brandon.choy.2037@scis.smu.edu.sg",
                "+65 8746 3847",
                "Mr");
    }
    private WingitUser createSampleUser2() {
        return new WingitUser(
                "password",
                "Jared",
                "Hong",
                Date.valueOf(LocalDate.parse("1996-10-03")),
                "jared.hong.2034@scis.smu.edu.sg",
                "+65 8455 0750",
                "Mrs");
    }

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void getAllUsers_Empty_Success() throws Exception {
        URI uri = constructUri("users");
        ResponseEntity<WingitUser[]> responseEntity = testRestTemplate.getForEntity(uri, WingitUser[].class);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(0, Objects.requireNonNull(responseEntity.getBody()).length);
    }

    @Test
    void getAllUsers_TwoUsers_Success() throws Exception {
        WingitUser[] sampleUsers = {
                createSampleUser1(),
                createSampleUser2()
        };
        Integer[] userIds = {
                userRepository.save(sampleUsers[0]).getUserId(),
                userRepository.save(sampleUsers[1]).getUserId()
        };


        URI uri = constructUri("users");
        ResponseEntity<WingitUser[]> responseEntity = testRestTemplate.getForEntity(uri, WingitUser[].class);
        WingitUser[] retrievedUsers = responseEntity.getBody();
        // Logger.getLogger("UserControllerTest").log(Level.INFO, "QQQ: Length = " + retrievedUsers.length + "\n" + Arrays.toString(retrievedUsers));

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(2, retrievedUsers.length);

        for (int i = 0; i < retrievedUsers.length; i++) {
            Integer id = userIds[i];
            WingitUser sampleControl = sampleUsers[i];
            WingitUser retrievedUser = retrievedUsers[i];

            assertEquals(id, retrievedUser.getUserId());
            assertEquals(sampleControl.getPassword(), retrievedUser.getPassword());
            assertEquals(sampleControl.getFirstName(), retrievedUser.getFirstName());
            assertEquals(sampleControl.getLastName(), retrievedUser.getLastName());
            assertEquals(sampleControl.getDob(), retrievedUser.getDob());
            assertEquals(sampleControl.getEmail(), retrievedUser.getEmail());
            assertEquals(sampleControl.getPhone(), retrievedUser.getPhone());
            assertEquals(sampleControl.getSalutation(), retrievedUser.getSalutation());

        }
    }

    @Test
    void getUser_Success() throws Exception {
        WingitUser[] sampleUsers = {
                createSampleUser1(),
                createSampleUser2()
        };
        Integer[] userIds = {
                userRepository.save(sampleUsers[0]).getUserId(),
                userRepository.save(sampleUsers[1]).getUserId()
        };
        final int sampleIndexUsedForTesting = 1;

        URI uri = constructUri("users/" + userIds[sampleIndexUsedForTesting]);
        ResponseEntity<WingitUser> responseEntity = testRestTemplate.getForEntity(uri, WingitUser.class);
        WingitUser retrievedUser = responseEntity.getBody();
        // Logger.getLogger("UserControllerTest").log(Level.INFO, "QQQ: " + retrievedUser);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(retrievedUser);
        WingitUser sampleControl = sampleUsers[sampleIndexUsedForTesting];

        assertEquals(userIds[sampleIndexUsedForTesting], retrievedUser.getUserId());
        assertEquals(sampleControl.getPassword(), retrievedUser.getPassword());
        assertEquals(sampleControl.getFirstName(), retrievedUser.getFirstName());
        assertEquals(sampleControl.getLastName(), retrievedUser.getLastName());
        assertEquals(sampleControl.getDob(), retrievedUser.getDob());
        assertEquals(sampleControl.getEmail(), retrievedUser.getEmail());
        assertEquals(sampleControl.getPhone(), retrievedUser.getPhone());
        assertEquals(sampleControl.getSalutation(), retrievedUser.getSalutation());
    }

    @Test
    void getUser_Failure() throws Exception {
        URI uri = constructUri("users/1");
        ResponseEntity<WingitUser> responseEntity = testRestTemplate.getForEntity(uri, WingitUser.class);
        WingitUser retrievedUser = responseEntity.getBody();
        // Logger.getLogger("UserControllerTest").log(Level.INFO, "QQQ: " + retrievedUser);

        assertEquals(404, responseEntity.getStatusCode().value());
    }

    @Test
    void createUser_Success() throws Exception {
        WingitUser sampleUser = createSampleUser1();
        URI uri = constructUri("users/new");
        ResponseEntity<WingitUser> responseEntity = testRestTemplate.postForEntity(uri, sampleUser, WingitUser.class);

        assertEquals(201, responseEntity.getStatusCode().value());
        WingitUser postedUser = userRepository.findByEmail(sampleUser.getEmail());
        assertNotNull(postedUser);
    }

    @Test
    void createUser_ExistingEmail_Failure() throws Exception {
        WingitUser existingUser = createSampleUser1();
        userRepository.save(existingUser);

        WingitUser duplicateUserEmail = createSampleUser2();
        duplicateUserEmail.setEmail(existingUser.getEmail());
        URI uri = constructUri("users/new");
        ResponseEntity<WingitUser> responseEntity = testRestTemplate.postForEntity(uri, duplicateUserEmail, WingitUser.class);

        assertEquals(400, responseEntity.getStatusCode().value());
    }

    // TODO: Testcase createUser with future Date of Birth
    // TODO: Testcase createUser with no name, empty parameters, etc.
    // TODO: Testcase createUser with non-valid parameters, Salutation, Role, etc.

    @Test
    void deleteUser_Success() throws Exception {
        WingitUser userToBeDeleted = createSampleUser1();
        Integer userId = userRepository.save(userToBeDeleted).getUserId();

        URI uri = constructUri("users/delete/" + userId);
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(uri, HttpMethod.DELETE, null, Void.class);
        assertEquals(200, responseEntity.getStatusCode().value());

        Optional<WingitUser> retrievedUser = userRepository.findById(userId);
        assertFalse(retrievedUser.isPresent());
    }

    // TODO: Delete User fails test case (not found).

    @Test
    void updateUser_Success() throws Exception {
        WingitUser sampleUser = createSampleUser1();
        Integer sampleUserId = userRepository.save(sampleUser).getUserId();
        WingitUser updatedUser = createSampleUser1();
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        // TODO: Exhaustive test all the other update fields

        URI uri = constructUri("users/update/" + sampleUserId);
        HttpEntity<WingitUser> payloadEntity = new HttpEntity<>(updatedUser);
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(uri, HttpMethod.PUT, payloadEntity, Void.class);
        assertEquals(200, responseEntity.getStatusCode().value());

        WingitUser retrievedUser = userRepository.findById(sampleUserId).get();
        assertEquals("Updated", retrievedUser.getFirstName());
        assertEquals("User", retrievedUser.getLastName());
    }

    // TODO: Update User fail test case (not found).
}