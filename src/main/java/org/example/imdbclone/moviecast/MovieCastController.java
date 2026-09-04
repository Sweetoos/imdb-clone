package org.example.imdbclone.moviecast;

import lombok.RequiredArgsConstructor;
import org.example.imdbclone.moviecast.dto.MovieCastCreateDto;
import org.example.imdbclone.moviecast.dto.MovieCastResponseDto;
import org.example.imdbclone.moviecast.dto.MovieCastUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/casts")
@RequiredArgsConstructor
public class MovieCastController {
    private final MovieCastService movieCastService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieCastResponseDto createMovieCast(@RequestBody MovieCastCreateDto dto){
        return movieCastService.createMovieCast(dto);
    }

    @GetMapping("/title/{titleId}")
    public List<MovieCastResponseDto> getCastForTitle(@PathVariable Long titleId){
        return movieCastService.getCastForTitle(titleId);
    }

    @GetMapping("/person/{personId}")
    public List<MovieCastResponseDto> getFilmographyForPerson(@PathVariable Long personId){
        return movieCastService.getFilmographyForPerson(personId);
    }

    @PatchMapping("/{id}")
    public MovieCastResponseDto updateMovieCast(@PathVariable Long id, @RequestBody MovieCastUpdateDto dto){
        return movieCastService.updateCast(id,dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovieCast(@PathVariable Long id){
        movieCastService.deleteCast(id);
    }
}
