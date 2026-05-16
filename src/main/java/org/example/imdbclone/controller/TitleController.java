package org.example.imdbclone.controller;

import lombok.RequiredArgsConstructor;
import org.example.imdbclone.model.Title;
import org.example.imdbclone.service.TitleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/titles")
@RequiredArgsConstructor
public class TitleController {
    private final TitleService titleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Title saveTitle(@RequestBody Title title) {
        return titleService.createTitle(title);
    }

    @GetMapping
    public List<Title> getAllTitles() {
        return titleService.getAllTitles();
    }

    @GetMapping("/{id}")
    public Title getTitleById(@RequestBody Long id) {
        return titleService.getTitleById(id)
                .orElseThrow(() -> new RuntimeException("Couldn't get Title with ID: " + id));
    }

    @PutMapping("/{id}")
    public Title updateTitle(Long id, Title titleDetails) {
        return titleService.updateTitle(id, titleDetails);
    }

    @PatchMapping("/{id}")
    public Title patchTitle(@PathVariable Long id, Map<String, Object> updates){
        return titleService.patchtitle(id, updates);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTitle(@PathVariable Long id){
        titleService.deleteTitle(id);
    }
}
