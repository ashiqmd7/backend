package com.G2T5203.wingit;

import com.G2T5203.wingit.entities.WingitUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

public class TestUtils {
    private final int port;
    private final BCryptPasswordEncoder encoder;

    public TestUtils(int port, BCryptPasswordEncoder encoder) {
        this.port = port;
        this.encoder = encoder;
    }

    public URI constructUri(String path) throws URISyntaxException {
        String baseUrl = "http://localhost:";
        return new URI(baseUrl + port + "/" + path);
    }
    // Helper functions
    public WingitUser createSampleUser1() {
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
    public WingitUser createSampleUser2() {
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
    public WingitUser createAdminUser() {
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
}
