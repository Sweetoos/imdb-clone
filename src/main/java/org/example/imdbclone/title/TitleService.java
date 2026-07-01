package org.example.imdbclone.title;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.imdbclone.title.domain.Title;
import org.example.imdbclone.title.dto.TitleCreateDto;
import org.example.imdbclone.title.dto.TitlePatchDto;
import org.example.imdbclone.title.dto.TitleResponseDto;
import org.example.imdbclone.title.dto.TitleUpdateDto;
import org.example.imdbclone.title.exception.TitleNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TitleService {
    private final TitleRepository titleRepository;

    @Transactional
    public TitleResponseDto createTitle(TitleCreateDto dto) {
        boolean exists = titleRepository.existsByTitleNameAndStartYear(dto.titleName(), dto.startYear());
        if (exists) {
            throw new RuntimeException("Title already exists");
        }

        if (dto.startYear() > Year.now().getValue() + 10) {
            throw new RuntimeException("Title can't be created in the future");
        }

        Title titleToSave = new Title();
        titleToSave.setTitleName(dto.titleName());
        titleToSave.setExplicitContent(dto.explicitContent());
        titleToSave.setRuntimeMinutes(dto.runtimeMinutes());
        titleToSave.setStartYear(dto.startYear());
        titleToSave.setEndYear(dto.endYear());
        titleToSave.setTitleType(dto.titleType());

        Title savedTitle = titleRepository.save(titleToSave);
        return mapToResponseDto(savedTitle);
    }

    public List<TitleResponseDto> getAllTitles() {
        List<Title> titles= titleRepository.findAll();
        return titles.stream().map(this::mapToResponseDto).toList();
    }

    public TitleResponseDto getTitleById(Long id) {
        Title title = titleRepository.findById(id).orElseThrow(() -> new TitleNotFoundException(id));
        return mapToResponseDto(title);
    }

    @Transactional
    public TitleResponseDto updateTitle(Long id, TitleUpdateDto dto) {
        Title title = titleRepository.findById(id).orElseThrow(() -> new TitleNotFoundException(id));

        title.setTitleName(dto.titleName());
        title.setExplicitContent(dto.explicitContent());
        title.setRuntimeMinutes(dto.runtimeMinutes());
        title.setStartYear(dto.startYear());
        title.setEndYear(dto.endYear());
        title.setTitleType(dto.titleType());
        return mapToResponseDto(title);
    }

    private TitleResponseDto mapToResponseDto(Title title) {
        return new TitleResponseDto(
                title.getTitleId(),
                title.getTitleName(),
                title.isExplicitContent(),
                title.getRuntimeMinutes(),
                title.getStartYear(),
                title.getEndYear(),
                title.getTitleType(),
                0.0
        );
    }

    @Transactional
    public TitleResponseDto patchTitle(Long id, TitlePatchDto dto) {
        Title title = titleRepository.findById(id)
                .orElseThrow(() -> new TitleNotFoundException(id));

        if (dto.titleName() != null) {
            title.setTitleName(dto.titleName());
        }
        if (dto.explicitContent() != null) {
            title.setExplicitContent(dto.explicitContent());
        }
        if (dto.runtimeMinutes() != null) {
            title.setRuntimeMinutes(dto.runtimeMinutes());
        }
        if (dto.startYear() != null) {
            title.setStartYear(dto.startYear());
        }
        if (dto.endYear() != null) {
            title.setEndYear(dto.endYear());
        }
        if (dto.titleType() != null) {
            title.setTitleType(dto.titleType());
        }

        return mapToResponseDto(title);
    }

    @Transactional
    public void deleteTitle(Long id) {
        boolean exists = titleRepository.existsById(id);
        if (!exists) {
            throw new TitleNotFoundException(id);
        }
        titleRepository.deleteById(id);
    }
}
