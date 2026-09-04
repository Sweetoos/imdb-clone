package org.example.imdbclone.keyword;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.imdbclone.keyword.dto.KeywordRequestDto;
import org.example.imdbclone.keyword.dto.KeywordResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/keywords")
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public KeywordResponseDto createKeyword(@RequestBody @Valid KeywordRequestDto dto) {
        return keywordService.createKeyword(dto);
    }

    @GetMapping
    public List<KeywordResponseDto> getAllKeywords() {
        return keywordService.getAllKeywords();
    }

    @GetMapping("/{id}")
    public KeywordResponseDto getKeywordById(@PathVariable Long id) {
        return keywordService.getKeywordById(id);
    }

    @PutMapping("/{id}")
    public KeywordResponseDto updateKeyword(@PathVariable Long id, @RequestBody @Valid KeywordRequestDto dto) {
        return keywordService.updateKeyword(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteKeyword(@PathVariable Long id) {
        keywordService.deleteKeyword(id);
    }
}