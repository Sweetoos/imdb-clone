package org.example.imdbclone.moviecast;

import lombok.RequiredArgsConstructor;
import org.example.imdbclone.moviecast.domain.MovieCast;
import org.example.imdbclone.moviecast.dto.MovieCastCreateDto;
import org.example.imdbclone.moviecast.dto.MovieCastResponseDto;
import org.example.imdbclone.moviecast.dto.MovieCastUpdateDto;
import org.example.imdbclone.moviecast.exception.MovieCastNotFoundException;
import org.example.imdbclone.person.PersonRepository;
import org.example.imdbclone.person.domain.Person;
import org.example.imdbclone.title.TitleRepository;
import org.example.imdbclone.title.domain.Title;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieCastService {
    private final MovieCastRepository movieCastRepository;
    private final TitleRepository titleRepository;
    private final PersonRepository personRepository;

    @Transactional
    public MovieCastResponseDto createMovieCast(MovieCastCreateDto dto) {
        Title title = titleRepository.findById(dto.titleId()).orElseThrow(() -> new RuntimeException("Title not found with id " + dto.titleId()));
        Person person = personRepository.findById(dto.personId()).orElseThrow(() -> new RuntimeException("Person not found with id " + dto.personId()));

        boolean exists = movieCastRepository.existsByTitleTitleIdAndPersonPersonIdAndJobRole(dto.titleId(), dto.personId(), dto.jobRole());
        if (exists) {
            throw new RuntimeException("Person with id " + dto.personId() + " already has cast for title with id " + dto.titleId());
        }
        MovieCast movieCast = MovieCast.builder()
                .title(title)
                .person(person)
                .characterName(dto.characterName())
                .jobRole(dto.jobRole())
                .build();

        MovieCast saved = movieCastRepository.save(movieCast);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<MovieCastResponseDto> getCastForTitle(Long titleId) {
        return movieCastRepository.findByTitleTitleId(titleId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MovieCastResponseDto> getFilmographyForPerson(Long personId){
        return movieCastRepository.findByPersonPersonId(personId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    public MovieCastResponseDto updateCast(Long id, MovieCastUpdateDto dto) {
        MovieCast movieCast = movieCastRepository.findById(id)
                .orElseThrow(() -> new MovieCastNotFoundException(id));

        if (dto.characterName() != null) {
            movieCast.setCharacterName(dto.characterName());
        }
        if (dto.jobRole() != null) {
            movieCast.setJobRole(dto.jobRole());
        }

        return mapToDto(movieCast);
    }

    @Transactional
    public void deleteCast(Long id) {
        if (!movieCastRepository.existsById(id)) {
            throw new IllegalArgumentException("MovieCast not found with id: " + id);
        }
        movieCastRepository.deleteById(id);
    }

    private MovieCastResponseDto mapToDto(MovieCast movieCast) {
        return new MovieCastResponseDto(
                movieCast.getId(),
                movieCast.getTitle().getTitleId(),
                movieCast.getTitle().getTitleName(),
                movieCast.getPerson().getPersonId(),
                movieCast.getPerson().getFirstName() + " " + movieCast.getPerson().getLastName(),
                movieCast.getCharacterName(),
                movieCast.getJobRole()
        );
    }
}
