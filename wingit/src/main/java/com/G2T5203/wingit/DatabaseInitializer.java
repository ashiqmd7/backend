package com.G2T5203.wingit;

import com.G2T5203.wingit.entities.*;
import com.G2T5203.wingit.plane.PlaneRepository;
import com.G2T5203.wingit.route.RouteRepository;
import com.G2T5203.wingit.routeListing.RouteListingRepository;
import com.G2T5203.wingit.user.UserRepository;
import com.G2T5203.wingit.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Class is used only in DEV profile to pre-populate it with data for testing purposes.

public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static void Log(String msg) { logger.debug(msg); }
    public static void init(ApplicationContext context) {
        // Initialise WingitUsers
        UserRepository userRepository = context.getBean(UserRepository.class);
        List<WingitUser> wingitUserList = new ArrayList<>();
        // TODO: Consider changing Wingit.DoB to LocalDate
        wingitUserList.add(userRepository.save(new WingitUser(
                "goodpassword",
                "Brandon",
                "Choy",
                DateUtils.handledParseDate("2001-12-04"),
                "brandon.choy.2037@scis.smu.edu.sg",
                "+65 8746 3847",
                "Mr")));
        wingitUserList.add(userRepository.save(new WingitUser(
                "password",
                "Jared",
                "Hong",
                DateUtils.handledParseDate("1996-10-03"),
                "jared.hong.2034@scis.smu.edu.sg",
                "+65 8455 0750",
                "Mrs")));
        for (WingitUser wingitUser : wingitUserList) {
            Log("[Add WingitUser]: " + wingitUser);
        }


        // Initialise Planes
        PlaneRepository planeRepository = context.getBean(PlaneRepository.class);
        List<Plane> planeList = new ArrayList<>();
        planeList.add(planeRepository.save(new Plane(
                "SQ123",
                100,
                "B777")));
        planeList.add(planeRepository.save(new Plane(
                "SQ888",
                200,
                "A350")));
        planeList.add(planeRepository.save(new Plane(
                "SQ364",
                100,
                "B777")));
        planeList.add(planeRepository.save(new Plane(
                "SQ39",
                200,
                "A350")));
        for (Plane plane : planeList) {
            Log("[Add Plane]: " + plane);
        }


        // Initialise Routes
        RouteRepository routeRepository = context.getBean(RouteRepository.class);
        List<Route> routeList = new ArrayList<>();
        routeList.add(routeRepository.save(new Route(
                "Singapore",
                "Taiwan",
                Duration.ofHours(4).plusMinutes(30) )));
        routeList.add(routeRepository.save(new Route(
                "Taiwan",
                "Singapore",
                Duration.ofHours(4).plusMinutes(45) )));
        routeList.add(routeRepository.save(new Route(
                "Singapore",
                "Japan",
                Duration.ofHours(7).plusMinutes(20) )));
        routeList.add(routeRepository.save(new Route(
                "Japan",
                "Singapore",
                Duration.ofHours(8).plusMinutes(10) )));
        for (Route route : routeList) {
            Log("[Add Route]: " + route);
        }


        // Initialise RouteListings
        RouteListingRepository routeListingRepository = context.getBean(RouteListingRepository.class);
        List<RouteListing> routeListingList = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            String morningDatetimeStr = String.format("2023-12-%02d 07:15:00", i);
            Date morningDatetime = DateUtils.handledParseDateTime(morningDatetimeStr);
            // System.out.println("QQQ: " + morningDatetimeStr + " --> " + morningDatetime);
            routeListingList.add(routeListingRepository.save(new RouteListing(
                    new RouteListingPk(
                            planeList.get(0),
                            routeList.get(0),
                            morningDatetime),
                    643.34 + i * 12.34
            )));
            routeListingList.add(routeListingRepository.save(new RouteListing(
                    new RouteListingPk(
                            planeList.get(2),
                            routeList.get(2),
                            morningDatetime),
                    823.08 + i * 12.34
            )));

            String afternoonDatetimeStr = String.format("2023-12-%02d 16:30:00", i);
            Date afternoonDatetime = DateUtils.handledParseDateTime(afternoonDatetimeStr);
            routeListingList.add(routeListingRepository.save(new RouteListing(
                    new RouteListingPk(
                            planeList.get(0),
                            routeList.get(1),
                            afternoonDatetime),
                    643.34 + i * 12.34
            )));
            routeListingList.add(routeListingRepository.save(new RouteListing(
                    new RouteListingPk(
                            planeList.get(3),
                            routeList.get(3),
                            afternoonDatetime),
                    823.08 + i * 12.34
            )));
        }
        for (RouteListing routeListing : routeListingList) {
            Log("[Add RouteListing]: " + routeListing);
        }
    }
}
