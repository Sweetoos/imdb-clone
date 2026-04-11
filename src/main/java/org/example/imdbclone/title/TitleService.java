package org.example.imdbclone.title;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TitleService {
    private final EntityManager entityManager;
    private final TitleRepository titleRepository;

    public List<Title> listAllTitles(){
        return titleRepository.findAll();
    }
}
