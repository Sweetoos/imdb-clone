package org.example.imdbclone.genre;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.imdbclone.genre.dto.GenreRequestDto;
import org.example.imdbclone.genre.dto.GenreResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenreResponseDto createGenre(@RequestBody @Valid GenreRequestDto dto) {
        return genreService.createGenre(dto);
    }

    @GetMapping
    public List<GenreResponseDto> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public GenreResponseDto getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }

    @PutMapping("/{id}")
    public GenreResponseDto updateGenre(@PathVariable Long id, @RequestBody @Valid GenreRequestDto dto) {
        return genreService.updateGenre(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
    }
}
