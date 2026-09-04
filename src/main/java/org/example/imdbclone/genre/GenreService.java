package org.example.imdbclone.genre;


import lombok.RequiredArgsConstructor;
import org.example.imdbclone.genre.domain.Genre;
import org.example.imdbclone.genre.dto.GenreRequestDto;
import org.example.imdbclone.genre.dto.GenreResponseDto;
import org.example.imdbclone.genre.exception.GenreAlreadyExistsException;
import org.example.imdbclone.genre.exception.GenreNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreService {

    private final GenreRepository genreRepository;

    @Transactional
    public GenreResponseDto createGenre(GenreRequestDto dto) {
        if (genreRepository.existsByGenreNameIgnoreCase(dto.genreName())) {
            throw new GenreAlreadyExistsException(dto.genreName());
        }

        Genre genre = Genre.builder()
                .genreName(dto.genreName())
                .build();

        Genre saved = genreRepository.save(genre);
        return mapToDto(saved);
    }

    public List<GenreResponseDto> getAllGenres() {
        return genreRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public GenreResponseDto getGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException(id));
        return mapToDto(genre);
    }

    @Transactional
    public GenreResponseDto updateGenre(Long id, GenreRequestDto dto) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException(id));

        if (!genre.getGenreName().equalsIgnoreCase(dto.genreName()) &&
                genreRepository.existsByGenreNameIgnoreCase(dto.genreName())) {
            throw new GenreAlreadyExistsException(dto.genreName());
        }

        genre.setGenreName(dto.genreName());
        return mapToDto(genre);
    }

    @Transactional
    public void deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new GenreNotFoundException(id);
        }
        genreRepository.deleteById(id);
    }

    private GenreResponseDto mapToDto(Genre genre) {
        return new GenreResponseDto(genre.getGenreId(), genre.getGenreName());
    }
}
