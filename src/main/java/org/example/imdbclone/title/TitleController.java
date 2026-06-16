package org.example.imdbclone.title;

import lombok.RequiredArgsConstructor;
import org.example.imdbclone.title.domain.Title;
import org.example.imdbclone.title.dto.TitleCreateDto;
import org.example.imdbclone.title.dto.TitlePatchDto;
import org.example.imdbclone.title.dto.TitleResponseDto;
import org.example.imdbclone.title.dto.TitleUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/titles")
@RequiredArgsConstructor
public class TitleController {
    private final TitleService titleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TitleResponseDto saveTitle(@RequestBody TitleCreateDto title) {
        return titleService.createTitle(title);
    }

    @GetMapping
    public List<TitleResponseDto> getAllTitles() {
        return titleService.getAllTitles();
    }

    @GetMapping("/{id}")
    public TitleResponseDto getTitleById(@PathVariable Long id) {
        return titleService.getTitleById(id);
    }

    @PutMapping("/{id}")
    public TitleResponseDto updateTitle(@PathVariable Long id, @RequestBody TitleUpdateDto dto) {
        return titleService.updateTitle(id, dto);
    }

    @PatchMapping("/{id}")
    public TitleResponseDto patchTitle(@PathVariable Long id, @RequestBody TitlePatchDto dto) {
        return titleService.patchTitle(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTitle(@PathVariable Long id) {
        titleService.deleteTitle(id);
    }
}
