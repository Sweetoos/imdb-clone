package org.example.imdbclone.person;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.imdbclone.person.dto.PersonResponseDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {
}
