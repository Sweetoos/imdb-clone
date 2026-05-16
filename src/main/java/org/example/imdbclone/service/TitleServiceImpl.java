package org.example.imdbclone.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.imdbclone.exception.ResourceNotFoundException;
import org.example.imdbclone.model.Title;
import org.example.imdbclone.model.TitleType;
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

        if(updates.containsKey("title_name")){
            title.setTitleName((String) updates.get("title_name"));
        }

        if(updates.containsKey("start_year")){
            title.setStartYear((Integer) updates.get("start_year"));
        }

        if(updates.containsKey("end_year")){
            title.setEndYear((Integer) updates.get("end_year"));
        }

        if(updates.containsKey("explicit_content")){
            title.setExplicitContent((Boolean) updates.get("explicit_content"));
        }

        if(updates.containsKey("runtime_minutes")){
            title.setRuntimeMinutes((Integer) updates.get("runtime_minutes"));
        }

        if(updates.containsKey("title_type")){
            title.setTitleType((TitleType) updates.get("title_type"));
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
