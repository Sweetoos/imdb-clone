package org.example.imdbclone.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.imdbclone.model.Title;
import org.example.imdbclone.repository.TitleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TitleService {
    Title createTitle(Title title);
    List<Title> getAllTitles();
    Optional<Title> getTitleById(Long id);
    Title updateTitle(Long id, Title titleDetails);
    Title patchtitle(Long id, Map<String, Object> updates);
    void deleteTitle(Long id);
}
