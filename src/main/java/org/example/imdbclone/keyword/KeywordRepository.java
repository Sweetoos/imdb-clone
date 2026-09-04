package org.example.imdbclone.keyword;

import org.example.imdbclone.keyword.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}