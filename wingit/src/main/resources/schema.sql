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
    ROUTE_ID bigint auto_increment not null,
    DEPARTURE_DEST varchar(255) not null,
    ARRIVAL_DEST varchar(255) not null,
    FLIGHT_DURATION double not null,
    primary key (ROUTE_ID)
);

create table ROUTE_LISTING (
    PLANE_ID varchar(6) not null,
    ROUTE_ID bigint not null,
    DEPARTURE_DATETIME datetime not null,
    BASE_PRICE double not null,
    primary key (PLANE_ID, ROUTE_ID, DEPARTURE_DATETIME),
    foreign key (PLANE_ID) references Plane(PLANE_ID),
    foreign key (ROUTE_ID) references Route(ROUTE_ID)
);

create table BOOKING (
     BOOKING_ID varchar(6) not null,
     USER_ID bigint not null,

     OUTBOUND_PLANE_ID varchar(6) not null,
     OUTBOUND_ROUTE_ID bigint not null,
     OUTBOUND_DEPARTURE_DATETIME datetime not null,

     INBOUND_PLANE_ID varchar(6) not null,
     INBOUND_ROUTE_ID bigint not null,
     INBOUND_DEPARTURE_DATETIME datetime not null,

     START_BOOKING_DATETIME datetime not null,
     PARTY_SIZE bigint not null,
     CHARGED_PRICE double not null,
     primary key (BOOKING_ID),
     foreign key (USER_ID) references WINGIT_USER(USER_ID),
     foreign key (OUTBOUND_PLANE_ID, OUTBOUND_ROUTE_ID, OUTBOUND_DEPARTURE_DATETIME) references ROUTE_LISTING(PLANE_ID, ROUTE_ID, DEPARTURE_DATETIME),
     foreign key (INBOUND_PLANE_ID, INBOUND_ROUTE_ID, INBOUND_DEPARTURE_DATETIME) references ROUTE_LISTING(PLANE_ID, ROUTE_ID, DEPARTURE_DATETIME)
);

create table SEAT_LISTING (
     PLANE_ID varchar(6) not null,
     ROUTE_ID bigint not null,
     DEPARTURE_DATETIME datetime not null,

     SEAT_NUMBER bigint not null,
     BOOKING_ID varchar(6) not null,
     OCCUPANT_NAME varchar(255),
     primary key (PLANE_ID, ROUTE_ID, DEPARTURE_DATETIME, SEAT_NUMBER),
     foreign key (PLANE_ID, ROUTE_ID, DEPARTURE_DATETIME) references ROUTE_LISTING(PLANE_ID, ROUTE_ID, DEPARTURE_DATETIME),
     foreign key (PLANE_ID, SEAT_NUMBER) references SEAT(PLANE_ID, SEAT_NUMBER),
     foreign key (BOOKING_ID) references BOOKING(BOOKING_ID)
);