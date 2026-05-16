package org.example.imdbclone.repository;

import org.example.imdbclone.model.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TitleRepository extends JpaRepository<Title,Long> {
    Optional<Title> findById(Long id);
}
