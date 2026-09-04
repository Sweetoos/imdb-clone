package org.example.imdbclone.moviecast.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

public enum JobRole {
    ACTOR,
    DIRECTOR,
    WRITER,
    PRODUCER
}