package org.example.imdbclone.keyword;

import lombok.RequiredArgsConstructor;
import org.example.imdbclone.keyword.domain.Keyword;
import org.example.imdbclone.keyword.dto.KeywordRequestDto;
import org.example.imdbclone.keyword.dto.KeywordResponseDto;
import org.example.imdbclone.keyword.exception.KeywordAlreadyExistsException;
import org.example.imdbclone.keyword.exception.KeywordNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional
    public KeywordResponseDto createKeyword(KeywordRequestDto dto) {
        if (keywordRepository.existsByNameIgnoreCase(dto.name())) {
            throw new KeywordAlreadyExistsException(dto.name());
        }

        Keyword keyword = Keyword.builder()
                .name(dto.name())
                .build();

        Keyword saved = keywordRepository.save(keyword);
        return mapToDto(saved);
    }

    public List<KeywordResponseDto> getAllKeywords() {
        return keywordRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public KeywordResponseDto getKeywordById(Long id) {
        Keyword keyword = keywordRepository.findById(id)
                .orElseThrow(() -> new KeywordNotFoundException(id));
        return mapToDto(keyword);
    }

    @Transactional
    public KeywordResponseDto updateKeyword(Long id, KeywordRequestDto dto) {
        Keyword keyword = keywordRepository.findById(id)
                .orElseThrow(() -> new KeywordNotFoundException(id));

        if (!keyword.getName().equalsIgnoreCase(dto.name()) &&
                keywordRepository.existsByNameIgnoreCase(dto.name())) {
            throw new KeywordAlreadyExistsException(dto.name());
        }

        keyword.setName(dto.name());
        return mapToDto(keyword);
    }

    @Transactional
    public void deleteKeyword(Long id) {
        if (!keywordRepository.existsById(id)) {
            throw new KeywordNotFoundException(id);
        }
        keywordRepository.deleteById(id);
    }

    private KeywordResponseDto mapToDto(Keyword keyword) {
        return new KeywordResponseDto(keyword.getKeywordId(), keyword.getName());
    }
}