create table title
(
    title_id        serial                not null primary key,
    title_type      varchar(20)           not null,
    primary_title   varchar(500)          not null,
    original_title  varchar(500),
    is_adult        boolean default false not null,
    start_year      int,
    end_year        int,
    runtime_minutes int
);

create table name
(
    name_id    serial       not null primary key,
    first_name varchar(100) not null,
    last_name  varchar(100) not null,
    birth_date date,
    death_date date
);

create table genre
(
    genre_id   serial              not null primary key,
    genre_name varchar(100) unique not null
);

create table title_genre
(
    title_id int not null,
    genre_id int not null,
    primary key (title_id, genre_id),
    constraint fk_title foreign key (title_id) references title (title_id) on delete cascade,
    constraint fk_genre foreign key (genre_id) references genre (genre_id) on delete cascade
);

create table title_principals
(
    title_id int          not null,
    name_id  int          not null,
    ordering int          not null,
    category varchar(100) not null,
    job      varchar(255),
    characters text,
    primary key (title_id, name_id, ordering),
    constraint fk_title foreign key (title_id) references title (title_id) on delete cascade,
    constraint fk_name foreign key (name_id) references name (name_id) on delete cascade
);

create table title_episode
(
    episode_id      int not null primary key,
    parent_title_id int not null,
    season_number   int,
    episode_number  int,
    constraint fk_episode foreign key (episode_id) references title (title_id) on delete cascade,
    constraint fk_parent_title_id foreign key (parent_title_id) references title (title_id) on delete cascade
);

create table title_rating
(
    title_id       int primary key not null,
    average_rating decimal(3, 1) default 0.0 not null,
    num_votes      int           default 0   not null,
    constraint fk_title_id foreign key (title_id) references title (title_id) on delete cascade
);

create table users
(
    user_id       serial primary key      not null,
    username      varchar(50) unique      not null,
    email         varchar(255) unique     not null,
    password_hash varchar(255)            not null,
    created_at    timestamp default now() not null
);

create table reviews
(
    review_id   serial primary key      not null,
    user_id     int not null,
    title_id    int not null,
    rating      int not null check (rating >= 1 and rating <= 10),
    review_text text,
    created_at  timestamp default now() not null,
    constraint fk_user_id foreign key (user_id) references users (user_id) on delete cascade,
    constraint fk_title_id foreign key (title_id) references title (title_id) on delete cascade,
    constraint unique_user_review_per_movie unique (user_id, title_id)
);