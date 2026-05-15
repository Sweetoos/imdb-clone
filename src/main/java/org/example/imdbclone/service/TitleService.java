package org.example.imdbclone.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.imdbclone.model.Title;
import org.example.imdbclone.repository.TitleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TitleService {
    private final EntityManager entityManager;
    private final TitleRepository titleRepository;

    public Title createTitle(Title title){
        return titleRepository.save(title);
    }

    public void addPersonForTitle(Title title){
        titleRepository.save(title);
    }
}
