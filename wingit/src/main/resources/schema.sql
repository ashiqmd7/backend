create table WINGIT_USER (
    USER_ID bigint auto_increment not null,
    PASSWORD varchar(255) not null,
    FIRST_NAME varchar(255) not null,
    LAST_NAME varchar(255) not null,
    DOB date not null,
    EMAIL varchar(255) not null,
    PHONE varchar(20) not null,
    SALUTATION varchar(255) not null,
    primary key (USER_ID)
);

create table Plane (
                       planeID bigint not null,
                       capacity bigint not null,
                       model varchar(255) not null,
                       primary key (planeID)
);


create table Seat (
                      planeID bigint not null,
                      seatNumber bigint not null,
                      seatClass varchar(255) not null,
                      priceFactor double not null,
                      primary key (planeID, seatNumber),
                      foreign key (planeID) references Plane(planeID)
);

create table Route (
                       routeID bigint not null,
                       departureDest varchar(255) not null,
                       arrivalDest varchar(255) not null,
                       flightDuration double not null,
                       primary key (routeID)
);

create table RouteListing (
                              planeID bigint not null,
                              routeID bigint not null,
                              departureDatetime datetime not null,
                              basePrice double not null,
                              primary key (planeID, routeID),
                              foreign key (planeID) references Plane(planeID),
                              foreign key (routeID) references Route(routeID)
);

create table HoldBooking (
                             holdID bigint not null,
                             userID bigint not null,
                             startDatetime datetime not null,
                             partySize bigint not null,
                             routeListingID bigint not null,
                             planeID bigint not null,
                             routeID bigint not null,
                             primary key (holdID, userID),
                             foreign key (userID) references WINGIT_USER(USER_ID),
                             foreign key (routeID) references Route(routeID)
);

create table SeatListing (
                             SeatListingID bigint not null,
                             planeID bigint not null,
                             routeID bigint not null,
                             seatNumber bigint not null,
                             occupantName varchar(255),
                             userID bigint,
                             bookingID bigint,
                             primary key (planeID, routeID, planeID, seatNumber),
                             foreign key (planeID, routeID) references RouteListing(planeID, routeID),
                             foreign key (planeID, seatNumber) references Seat(planeID, seatNumber)
);

create table TripBooking (
                             bookingID bigint not null,
                             chargedPrice double not null,
                             primary key (bookingID)
);