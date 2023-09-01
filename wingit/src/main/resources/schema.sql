create table wingit_user (
    USER_ID bigint not null,
    password varchar(255) not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    dob date not null,
    email varchar(255) not null,
    phone bigint not null,
    salutation varchar(255) not null,
    primary key (USER_ID)
);

create table plane (
    PLANE_ID bigint not null,
    capacity bigint not null,
    model varchar(255) not null,
    primary key (PLANE_ID)
);