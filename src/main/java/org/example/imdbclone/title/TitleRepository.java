package org.example.imdbclone.title;

import org.example.imdbclone.title.domain.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface TitleRepository extends JpaRepository<Title,Long> {
    boolean existsByTitleNameAndStartYear(String titleName, Integer startYear);
}
