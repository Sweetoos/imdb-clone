create table if not exists title
(
    title_id        bigserial             not null primary key,
    title_type      varchar(20)           not null,
    title_name      varchar(500)          not null,
    explicit_content boolean default false not null,
    start_year      int,
    end_year        int,
    runtime_minutes int
);

create table if not exists person
(
    person_id  bigserial    not null primary key,
    first_name varchar(100) not null,
    last_name  varchar(100) not null,
    birth_date date,
    death_date date,
    role       varchar(50)
);

create table if not exists movie_cast
(
    id             bigserial primary key,
    title_id       bigint not null,
    person_id      bigint not null,
    character_name varchar(255),
    job_role       varchar(50),
    constraint fk_cast_title foreign key (title_id) references title (title_id) on delete cascade,
    constraint fk_cast_person foreign key (person_id) references person (person_id) on delete cascade
);

create table if not exists genre
(
    genre_id   bigserial           not null primary key,
    genre_name varchar(100) unique not null
);

create table if not exists title_genre
(
    title_id bigint not null,
    genre_id bigint not null,
    primary key (title_id, genre_id),
    constraint fk_title foreign key (title_id) references title (title_id) on delete cascade,
    constraint fk_genre foreign key (genre_id) references genre (genre_id) on delete cascade
);

create table if not exists keyword
(
    keyword_id bigserial primary key,
    name       varchar(100) unique not null
);

create table if not exists title_keyword
(
    title_id   bigint not null,
    keyword_id bigint not null,
    primary key (title_id, keyword_id),
    constraint fk_title_kw foreign key (title_id) references title (title_id) on delete cascade,
    constraint fk_keyword_kw foreign key (keyword_id) references keyword (keyword_id) on delete cascade
);

create table if not exists title_rating
(
    title_id       bigint primary key        not null,
    average_rating double precision default 0.0 not null,
    num_votes      int           default 0   not null,
    constraint fk_rating_title foreign key (title_id) references title (title_id) on delete cascade
);

create table if not exists users
(
    user_id       bigserial primary key   not null,
    username      varchar(50) unique      not null,
    email         varchar(255) unique     not null,
    password_hash varchar(255)            not null,
    created_at    timestamp default now() not null
);

create table if not exists reviews
(
    review_id   bigserial primary key   not null,
    user_id     bigint                  not null,
    title_id    bigint                  not null,
    rating      int                     not null check (rating >= 1 and rating <= 10),
    review_text text,
    created_at  timestamp default now() not null,
    constraint fk_user_id foreign key (user_id) references users (user_id) on delete cascade,
    constraint fk_title_id foreign key (title_id) references title (title_id) on delete cascade,
    constraint unique_user_review_per_movie unique (user_id, title_id)
);

create table if not exists watchlist
(
    user_id  bigint not null,
    title_id bigint not null,
    added_at timestamp default now(),
    primary key (user_id, title_id),
    constraint fk_user foreign key (user_id) references users (user_id) on delete cascade,
    constraint fk_title foreign key (title_id) references title (title_id) on delete cascade
);

create table if not exists title_aka
(
    aka_id        bigserial primary key,
    title_id      bigint       not null,
    title_variant varchar(500) not null,
    region        varchar(10),
    language      varchar(20),
    constraint fk_title_aka foreign key (title_id) references title (title_id) on delete cascade
);

create table if not exists title_media
(
    media_id   bigserial primary key,
    title_id   bigint       not null,
    media_url  varchar(512) not null,
    media_type varchar(20),
    constraint fk_title_media foreign key (title_id) references title (title_id) on delete cascade
);

insert into genre (genre_name)
values ('Action'), ('Drama'), ('Sci-Fi'), ('Crime'), ('Thriller'), ('Comedy')
on conflict do nothing;

insert into keyword (name)
values ('dreams'), ('mafia'), ('atomic bomb'), ('drugs'), ('chemistry')
on conflict do nothing;

insert into person (first_name, last_name, birth_date, death_date, role)
values
    ('Christopher', 'Nolan', '1970-07-30', null, 'DIRECTOR'),
    ('Leonardo', 'DiCaprio', '1974-11-11', null, 'ACTOR'),
    ('Francis Ford', 'Coppola', '1939-04-07', null, 'DIRECTOR'),
    ('Bryan', 'Cranston', '1956-03-07', null, 'ACTOR'),
    ('Cillian', 'Murphy', '1976-05-25', null, 'ACTOR')
on conflict do nothing;

insert into title (title_type, title_name, explicit_content, start_year, end_year, runtime_minutes)
values
    ('MOVIE', 'Inception', false, 2010, null, 148),
    ('MOVIE', 'The Godfather', false, 1972, null, 175),
    ('MOVIE', 'Oppenheimer', false, 2023, null, 180),
    ('TVSERIES', 'Breaking Bad', false, 2008, 2013, 49)
on conflict do nothing;

insert into title_genre (title_id, genre_id)
values
    (1, 1), (1, 3), (1, 5),
    (2, 2), (2, 4),
    (3, 2), (3, 1),
    (4, 2), (4, 4), (4, 5)
on conflict do nothing;

insert into title_keyword (title_id, keyword_id)
values
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4), (4, 5)
on conflict do nothing;

insert into movie_cast (title_id, person_id, character_name, job_role)
values
    (1, 1, null, 'DIRECTOR'),
    (1, 2, 'Cobb', 'ACTOR'),
    (2, 3, null, 'DIRECTOR'),
    (4, 4, 'Walter White', 'ACTOR'),
    (3, 5, 'J. Robert Oppenheimer', 'ACTOR')
on conflict do nothing;

-- password_hash: password
insert into users (username, email, password_hash)
values
    ('john_doe', 'john@example.com', '$2a$10$w857Q91Tsz.K1q2d8y4bxe7p9V6Y.w60uDkKj9u7G1T9Q91Tsz.K1'),
    ('movie_fan', 'fan@example.com', '$2a$10$w857Q91Tsz.K1q2d8y4bxe7p9V6Y.w60uDkKj9u7G1T9Q91Tsz.K1')
on conflict do nothing;

insert into reviews (user_id, title_id, rating, review_text)
values
    (1, 1, 10, 'Mind-blowing masterpiece!'),
    (2, 1, 9, 'Great movie with fantastic soundtrack.'),
    (1, 2, 10, 'Absolute classic.');

insert into title_rating (title_id, average_rating, num_votes)
values
    (1, 9.5, 2),
    (2, 10.0, 1),
    (3, 0.0, 0),
    (4, 0.0, 0)
on conflict do nothing;

insert into watchlist (user_id, title_id)
values (1, 3), (2, 4)
on conflict do nothing;