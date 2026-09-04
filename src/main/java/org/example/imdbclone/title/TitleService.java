package org.example.imdbclone.title;

import lombok.RequiredArgsConstructor;
import org.example.imdbclone.genre.domain.Genre;
import org.example.imdbclone.keyword.domain.Keyword;
import org.example.imdbclone.title.domain.Title;
import org.example.imdbclone.title.dto.TitleCreateDto;
import org.example.imdbclone.title.dto.TitlePatchDto;
import org.example.imdbclone.title.dto.TitleResponseDto;
import org.example.imdbclone.title.dto.TitleUpdateDto;
import org.example.imdbclone.title.exception.TitleAlreadyExistsException;
import org.example.imdbclone.title.exception.TitleNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TitleService {

    private final TitleRepository titleRepository;

    @Transactional
    public TitleResponseDto createTitle(TitleCreateDto dto) {
        boolean exists = titleRepository.existsByTitleNameAndStartYear(dto.titleName(), dto.startYear());
        if (exists) {
            throw new TitleAlreadyExistsException("Title already exists with name: '" + dto.titleName() + "' and year: " + dto.startYear());
        }

        if (dto.startYear() != null && dto.startYear() > Year.now().getValue() + 10) {
            throw new IllegalArgumentException("Title cannot be created more than 10 years in the future");
        }

        if (dto.startYear() != null && dto.endYear() != null && dto.endYear() < dto.startYear()) {
            throw new IllegalArgumentException("End year cannot be before start year");
        }

        Title titleToSave = Title.builder()
                .titleName(dto.titleName())
                .explicitContent(dto.explicitContent())
                .runtimeMinutes(dto.runtimeMinutes())
                .startYear(dto.startYear())
                .endYear(dto.endYear())
                .titleType(dto.titleType())
                .build();

        Title savedTitle = titleRepository.save(titleToSave);
        return mapToResponseDto(savedTitle);
    }

    public List<TitleResponseDto> getAllTitles() {
        return titleRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public TitleResponseDto getTitleById(Long id) {
        Title title = titleRepository.findById(id)
                .orElseThrow(() -> new TitleNotFoundException(id));
        return mapToResponseDto(title);
    }

    @Transactional
    public TitleResponseDto updateTitle(Long id, TitleUpdateDto dto) {
        Title title = titleRepository.findById(id)
                .orElseThrow(() -> new TitleNotFoundException(id));

        title.setTitleName(dto.titleName());
        title.setExplicitContent(dto.explicitContent());
        title.setRuntimeMinutes(dto.runtimeMinutes());
        title.setStartYear(dto.startYear());
        title.setEndYear(dto.endYear());
        title.setTitleType(dto.titleType());

        return mapToResponseDto(title);
    }

    @Transactional
    public TitleResponseDto patchTitle(Long id, TitlePatchDto dto) {
        Title title = titleRepository.findById(id)
                .orElseThrow(() -> new TitleNotFoundException(id));

        Optional.ofNullable(dto.titleName()).ifPresent(title::setTitleName);
        Optional.ofNullable(dto.explicitContent()).ifPresent(title::setExplicitContent);
        Optional.ofNullable(dto.runtimeMinutes()).ifPresent(title::setRuntimeMinutes);
        Optional.ofNullable(dto.startYear()).ifPresent(title::setStartYear);
        Optional.ofNullable(dto.endYear()).ifPresent(title::setEndYear);
        Optional.ofNullable(dto.titleType()).ifPresent(title::setTitleType);

        return mapToResponseDto(title);
    }

    @Transactional
    public void deleteTitle(Long id) {
        if (!titleRepository.existsById(id)) {
            throw new TitleNotFoundException(id);
        }
        titleRepository.deleteById(id);
    }

    private TitleResponseDto mapToResponseDto(Title title) {
        Double avgRating = 0.0;
        Integer votes = 0;
        if (title.getTitleRating() != null) {
            avgRating = title.getTitleRating().getAverageRating();
            votes = title.getTitleRating().getNumVotes();
        }

        List<String> genreNames = title.getGenres() != null
                ? title.getGenres().stream().map(Genre::getGenreName).toList()
                : List.of();

        List<String> keywordNames = title.getKeywords() != null
                ? title.getKeywords().stream().map(Keyword::getName).toList()
                : List.of();

        List<TitleResponseDto.CastMemberDto> castList = title.getCast() != null
                ? title.getCast().stream()
                .map(c -> new TitleResponseDto.CastMemberDto(
                        c.getPerson().getPersonId(),
                        c.getPerson().getFirstName() + " " + c.getPerson().getLastName(),
                        c.getCharacterName(),
                        c.getJobRole()
                ))
                .toList()
                : List.of();

        return new TitleResponseDto(
                title.getTitleId(),
                title.getTitleName(),
                title.isExplicitContent(),
                title.getRuntimeMinutes(),
                title.getStartYear(),
                title.getEndYear(),
                title.getTitleType(),
                avgRating,
                votes,
                genreNames,
                keywordNames,
                castList
        );
    }
}