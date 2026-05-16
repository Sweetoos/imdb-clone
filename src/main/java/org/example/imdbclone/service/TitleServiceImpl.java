package org.example.imdbclone.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.imdbclone.exception.ResourceNotFoundException;
import org.example.imdbclone.model.Title;
import org.example.imdbclone.repository.TitleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TitleServiceImpl implements TitleService {
    private final EntityManager entityManager;
    private final TitleRepository titleRepository;

    @Override
    public Title createTitle(Title title) {
        return titleRepository.save(title);
    }

    @Override
    public List<Title> getAllTitles() {
        return titleRepository.findAll();
    }

    @Override
    public Optional<Title> getTitleById(Long id) {
        return titleRepository.findById(id);
    }

    @Override
    public Title updateTitle(Long id, Title titleDetails) {
        return titleRepository.findById(id)
                .map(existingTitle -> {
                    existingTitle.setTitleId(titleDetails.getTitleId());
                    existingTitle.setTitleName(titleDetails.getTitleName());
                    existingTitle.setTitleType(titleDetails.getTitleType());
                    existingTitle.setStartYear(titleDetails.getStartYear());
                    existingTitle.setEndYear(titleDetails.getEndYear());
                    existingTitle.setExplicitContent(titleDetails.isExplicitContent());
                    existingTitle.setRuntimeMinutes(titleDetails.getRuntimeMinutes());
                    return titleRepository.save(existingTitle);
                }).orElseThrow(() -> new RuntimeException("Title with ID " + id + " couldn't be found for update"));
    }

    @Override
    public Title patchtitle(Long id, Map<String, Object> updates) {
        Title title=titleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if(updates.containsKey("name")){
            title.setTitleName((String) updates.get("name"));
        }

        return titleRepository.save(title);
    }

    @Override
    public void deleteTitle(Long id) {
        if (!titleRepository.existsById(id)) {
            throw new RuntimeException("Title with ID " + id + " couldn't be found for delete");
        }
        titleRepository.deleteById(id);
    }
}
