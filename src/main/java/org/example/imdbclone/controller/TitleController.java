package org.example.imdbclone.controller;

import lombok.RequiredArgsConstructor;
import org.example.imdbclone.model.Title;
import org.example.imdbclone.service.TitleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/titles")
@RequiredArgsConstructor
public class TitleController {
    private final TitleService titleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Title saveTitle(@RequestBody Title title){
        return titleService.createTitle(title);
    }
}
