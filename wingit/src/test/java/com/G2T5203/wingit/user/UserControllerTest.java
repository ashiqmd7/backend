package com.G2T5203.wingit.user;

import com.G2T5203.wingit.entities.WingitUser;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @LocalServerPort
    private int port;

    private URI constructUri(String path) throws URISyntaxException {
        String baseUrl = "http://localhost:";
        return new URI(baseUrl + port + "/" + path);
    }
    // Helper functions
    private WingitUser createSampleUser1() {
        return new WingitUser(
                "brandonDaddy",
                encoder.encode("goodpassword"),
                "ROLE_USER",
                "Brandon",
                "Choy",
                LocalDate.parse("2001-12-04"),
                "brandon.choy.2037@scis.smu.edu.sg",
                "+65 8746 3847",
                "Mr");
    }
    private WingitUser createSampleUser2() {
        return new WingitUser(
                "DaddyChoy",
                encoder.encode("password"),
                "ROLE_USER",
                "Jared",
                "Hong",
                LocalDate.parse("1996-10-03"),
                "jared.hong.2034@scis.smu.edu.sg",
                "+65 8455 0750",
                "Mrs");
    }
    private WingitUser createAdminUser() {
        return new WingitUser(
                "admin",
                encoder.encode("pass"),
                "ROLE_ADMIN",
                "admin",
                "admin",
                LocalDate.parse("2000-01-01"),
                "admin@admin.com",
                "+65 6475 3846",
                "Master");
    }

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(createAdminUser());
        userRepository.save(createSampleUser1());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    void getAllUsers_TwoUsers_Success() throws Exception {
        WingitUser[] sampleUsers = {
                createAdminUser(),
                createSampleUser1()
        };

        URI uri = constructUri("users");
        ResponseEntity<WingitUser[]> responseEntity = testRestTemplate
                .withBasicAuth("admin", "pass")
                .getForEntity(uri, WingitUser[].class);
        WingitUser[] retrievedUsers = responseEntity.getBody();
        // Logger.getLogger("UserControllerTest").log(Level.INFO, "QQQ: Length = " + retrievedUsers.length + "\n" + Arrays.toString(retrievedUsers));

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(2, retrievedUsers.length);

        for (int i = 0; i < retrievedUsers.length; i++) {
            String id = sampleUsers[i].getUsername();
            WingitUser sampleControl = sampleUsers[i];
            WingitUser retrievedUser = retrievedUsers[i];

            assertEquals(id, retrievedUser.getUsername());
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
        WingitUser sampleUser = createSampleUser1();

        URI uri = constructUri("users/" + sampleUser.getUsername());
        ResponseEntity<WingitUser> responseEntity = testRestTemplate
                .withBasicAuth("admin", "pass")
                .getForEntity(uri, WingitUser.class);
        WingitUser retrievedUser = responseEntity.getBody();
        // Logger.getLogger("UserControllerTest").log(Level.INFO, "QQQ: " + retrievedUser);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(retrievedUser);

        assertEquals(sampleUser.getUsername(), retrievedUser.getUsername());
        assertEquals(sampleUser.getFirstName(), retrievedUser.getFirstName());
        assertEquals(sampleUser.getLastName(), retrievedUser.getLastName());
        assertEquals(sampleUser.getDob(), retrievedUser.getDob());
        assertEquals(sampleUser.getEmail(), retrievedUser.getEmail());
        assertEquals(sampleUser.getPhone(), retrievedUser.getPhone());
        assertEquals(sampleUser.getSalutation(), retrievedUser.getSalutation());
    }

    @Test
    void getUser_Failure() throws Exception {
        URI uri = constructUri("users/1");
        ResponseEntity<WingitUser> responseEntity = testRestTemplate
                .withBasicAuth("admin", "pass")
                .getForEntity(uri, WingitUser.class);
        WingitUser retrievedUser = responseEntity.getBody();
        // Logger.getLogger("UserControllerTest").log(Level.INFO, "QQQ: " + retrievedUser);

        assertEquals(404, responseEntity.getStatusCode().value());
    }

    @Test
    void createUser_Success() throws Exception {
        WingitUser sampleUser = createSampleUser2();
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
        // TODO: Assert that the passed message is also "Email already used for existing account."
    }

    @Test
    void createUser_ExistingUsername_Failure() throws Exception {
        WingitUser existingUser = createSampleUser1();
        userRepository.save(existingUser);

        WingitUser duplicateUsename = createSampleUser2();
        duplicateUsename.setUsername(existingUser.getUsername());
        URI uri = constructUri("users/new");
        ResponseEntity<WingitUser> responseEntity = testRestTemplate.postForEntity(uri, duplicateUsename, WingitUser.class);

        assertEquals(400, responseEntity.getStatusCode().value());
        // TODO: Assert that the passed message is also "Username already exists."
    }

    // TODO: Testcase createUser with future Date of Birth
    // TODO: Testcase createUser with no name, empty parameters, etc.
    // TODO: Testcase createUser with non-valid parameters, Salutation, Role, etc.

    @Test
    void deleteUser_Success() throws Exception {
        WingitUser userToBeDeleted = createSampleUser1();
        String username = userRepository.save(userToBeDeleted).getUsername();

        URI uri = constructUri("users/delete/" + username);
        ResponseEntity<Void> responseEntity = testRestTemplate
                .withBasicAuth("admin", "pass")
                .exchange(uri, HttpMethod.DELETE, null, Void.class);
        assertEquals(200, responseEntity.getStatusCode().value());

        Optional<WingitUser> retrievedUser = userRepository.findById(username);
        assertFalse(retrievedUser.isPresent());
    }

    @Test
    void deleteUser_NotFound_Failure() throws Exception {
        URI uri = constructUri("users/delete/1");
        ResponseEntity<Void> responseEntity = testRestTemplate
                .withBasicAuth("admin", "pass")
                .exchange(uri, HttpMethod.DELETE, null, Void.class);
        assertEquals(404, responseEntity.getStatusCode().value());
    }

    @Test
    void updateUser_Success() throws Exception {
        WingitUser sampleUser = createSampleUser1();
        String sampleUsername = userRepository.save(sampleUser).getUsername();
        WingitUser updatedUser = createSampleUser1();
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        // TODO: Exhaustive test all the other update fields

        URI uri = constructUri("users/update/" + sampleUsername);
        HttpEntity<WingitUser> payloadEntity = new HttpEntity<>(updatedUser);
        ResponseEntity<Void> responseEntity = testRestTemplate
                .withBasicAuth("admin", "pass")
                .exchange(uri, HttpMethod.PUT, payloadEntity, Void.class);
        assertEquals(200, responseEntity.getStatusCode().value());

        WingitUser retrievedUser = userRepository.findById(sampleUsername).get();
        assertEquals("Updated", retrievedUser.getFirstName());
        assertEquals("User", retrievedUser.getLastName());
    }

    // TODO: Update User fail test case (not found).


    @Test
    void updateUserPassword_Success() throws Exception {
        WingitUser sampleUser = createSampleUser1();
        final String NEW_PASSWORD = "newPassword";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("password", NEW_PASSWORD);
        HttpEntity<Map<String, Object>> payloadEntity = new HttpEntity<>(requestBody);

        URI uri = constructUri("users/updatePass/" + sampleUser.getUsername());
        ResponseEntity<Void> responseEntity = testRestTemplate
                .withBasicAuth("brandonDaddy", "goodpassword")
                .exchange(uri, HttpMethod.PUT, payloadEntity, Void.class);
        assertEquals(200, responseEntity.getStatusCode().value());
        ResponseEntity<Object> verificationEntity = testRestTemplate
                .withBasicAuth("brandonDaddy", NEW_PASSWORD)
                .getForEntity(constructUri("users/authTest/password_changed"), Object.class);
        assertEquals("{pathInput=password_changed}", verificationEntity.getBody().toString());
    }
}