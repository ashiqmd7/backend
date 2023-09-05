create table WINGIT_USER (
    USER_ID bigint auto_increment not null,
    PASSWORD varchar(255) not null,
    FIRST_NAME varchar(255) not null,
    LAST_NAME varchar(255) not null,
    DOB date not null,
    EMAIL varchar(255) UNIQUE not null,
    PHONE varchar(20) not null,
    SALUTATION varchar(255) not null,
    primary key (USER_ID)
);

create table PLANE (
    PLANE_ID varchar(6) not null,
    CAPACITY bigint not null,
    MODEL varchar(255) not null,
    primary key (PLANE_ID)
);


create table SEAT (
    PLANE_ID varchar(6) not null,
    SEAT_NUMBER bigint not null,
    SEAT_CLASS varchar(255) not null,
    PRICE_FACTOR double not null,
    primary key (PLANE_ID, SEAT_NUMBER),
    foreign key (PLANE_ID) references PLANE(PLANE_ID)
);

create table ROUTE (
    ROUTE_ID varchar(6) not null,
    DEPARTURE_DEST varchar(255) not null,
    ARRIVAL_DEST varchar(255) not null,
    FLIGHT_DURATION double not null,
    primary key (ROUTE_ID)
);

create table ROUTE_LISTING (
    PLANE_ID varchar(6) not null,
    ROUTE_ID varchar(6) not null,
    DEPARTURE_DATETIME datetime not null,
    BASE_PRICE double not null,
    primary key (PLANE_ID, ROUTE_ID),
    foreign key (PLANE_ID) references Plane(PLANE_ID),
    foreign key (ROUTE_ID) references Route(ROUTE_ID)
);

create table BOOKING (
     holdID bigint not null,
     userID bigint not null,
     startDatetime datetime not null,
     partySize bigint not null,
     routeListingID bigint not null,
     planeID bigint not null,
     routeID bigint not null,
     primary key (holdID, userID),
     foreign key (userID) references WINGIT_USER(USER_ID),
     foreign key (routeID) references Route(ROUTE_ID)
);

create table SEAT_LISTING (
     SeatListingID bigint not null,
     planeID bigint not null,
     routeID bigint not null,
     seatNumber bigint not null,
     occupantName varchar(255),
     userID bigint,
     bookingID bigint,
     primary key (planeID, routeID, planeID, seatNumber),
     foreign key (planeID, routeID) references ROUTE_LISTING(PLANE_ID, ROUTE_ID),
     foreign key (planeID, seatNumber) references SEAT(PLANE_ID, SEAT_NUMBER)
);