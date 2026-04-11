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
    title_id   int          not null,
    name_id    int          not null,
    ordering   int          not null,
    category   varchar(100) not null,
    job        varchar(255),
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
    title_id       int primary key           not null,
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
    user_id     int                     not null,
    title_id    int                     not null,
    rating      int                     not null check (rating >= 1 and rating <= 10),
    review_text text,
    created_at  timestamp default now() not null,
    constraint fk_user_id foreign key (user_id) references users (user_id) on delete cascade,
    constraint fk_title_id foreign key (title_id) references title (title_id) on delete cascade,
    constraint unique_user_review_per_movie unique (user_id, title_id)
);
create table watchlist
(
    user_id  int not null,
    title_id int not null,
    added_at timestamp default now(),
    primary key (user_id, title_id),
    constraint fk_user foreign key (user_id) references users (user_id) on delete cascade,
    constraint fk_title foreign key (title_id) references title (title_id) on delete cascade
);
create table keyword
(
    keyword_id serial primary key,
    name       varchar(100) unique not null
);

create table title_keyword
(
    title_id   int not null,
    keyword_id int not null,
    primary key (title_id, keyword_id),
    constraint fk_title foreign key (title_id) references title (title_id) on delete cascade,
    constraint fk_keyword foreign key (keyword_id) references keyword (keyword_id) on delete cascade
);
create table title_aka
(
    aka_id        serial primary key,
    title_id      int          not null,
    title_variant varchar(500) not null,
    region        varchar(10),
    language      varchar(10),
    constraint fk_title foreign key (title_id) references title (title_id) on delete cascade
);
create table title_media
(
    media_id   serial primary key,
    title_id   int          not null,
    media_url  varchar(512) not null,
    media_type varchar(20),
    constraint fk_title foreign key (title_id) references title (title_id) on delete cascade
);


-- values for the table

insert into genre (genre_name)
values ('Action'),
       ('Drama'),
       ('Sci-Fi'),
       ('Crime'),
       ('Thriller'),
       ('Comedy');

insert into name (first_name, last_name, birth_date, death_date)
values ('Christopher', 'Nolan', '1970-07-30', null),
       ('Leonardo', 'DiCaprio', '1974-11-11', null),
       ('Francis Ford', 'Coppola', '1939-04-07', null),
       ('Bryan', 'Cranston', '1956-03-07', null),
       ('Cillian', 'Murphy', '1976-05-25', null);

insert into title (title_type, primary_title, original_title, is_adult, start_year, end_year, runtime_minutes)
values ('movie', 'Inception', 'Inception', false, 2010, null, 148),
       ('movie', 'The Godfather', 'The Godfather', false, 1972, null, 175),
       ('movie', 'Oppenheimer', 'Oppenheimer', false, 2023, null, 180),
       ('tvSeries', 'Breaking Bad', 'Breaking Bad', false, 2008, 2013, 49),
       ('episode', 'Pilot', 'Pilot', false, 2008, null, 58);

insert into title_episode (episode_id, parent_title_id, season_number, episode_number)
values (5, 4, 1, 1);

insert into title_genre (title_id, genre_id)
values (1, 1),
       (1, 3),
       (1, 5),
       (2, 2),
       (2, 4),
       (3, 2),
       (3, 1),
       (4, 2),
       (4, 4),
       (4, 5);

insert into title_principals (title_id, name_id, ordering, category, job, characters)
values (1, 1, 1, 'director', null, null),
       (1, 2, 2, 'actor', null, 'Cobb'),
       (2, 3, 1, 'actor', null, 'Vito Corleone'),
       (4, 4, 1, 'actor', null, 'Walter White'),
       (3, 5, 1, 'actor', null, 'J. Robert Oppenheimer');

insert into title_rating (title_id, average_rating, num_votes)
values (1, 8.8, 2500000),
       (2, 9.2, 1900000),
       (3, 8.4, 600000),
       (4, 9.5, 2100000);

insert into users (username, email, password_hash)
values ('user_test', 'test@example.com', '$2a$12$R9h/lIPzHZ4.3.v.S.X.1e2.Z0L8.K9.K9.K9.K9.K9.K9.K9.K9.'),
       ('movie_fan', 'fan@example.com', '$2a$12$R9h/lIPzHZ4.3.v.S.X.1e2.Z0L8.K9.K9.K9.K9.K9.K9.K9.K9.');

insert into reviews (user_id, title_id, rating, review_text)
values (1, 1, 10, 'Mind-blowing masterpiece!'),
       (2, 1, 9, 'Great movie.'),
       (1, 2, 10, 'Absolute classic.');

insert into watchlist (user_id, title_id)
values (1, 3),
       (2, 4);

insert into keyword (name)
values ('dreams'),
       ('mafia'),
       ('atomic bomb'),
       ('drugs'),
       ('chemistry');

insert into title_keyword (title_id, keyword_id)
values (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (4, 5);

insert into title_aka (title_id, title_variant, region, language)
values (2, 'Ojciec Chrzestny', 'PL', 'pl'),
       (4, 'Breaking Bad: Sanando Mal', 'ES', 'es');

insert into title_media (title_id, media_url, media_type)
values (1, 'https://example.com/inception_poster.jpg', 'poster'),
       (1, 'https://youtube.com/watch?v=123', 'trailer'),
       (2, 'https://example.com/godfather.jpg', 'poster');