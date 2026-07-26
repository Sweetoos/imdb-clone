package org.example.imdbclone.person;


import lombok.RequiredArgsConstructor;
import org.example.imdbclone.person.dto.PersonCreateDto;
import org.example.imdbclone.person.dto.PersonPatchDto;
import org.example.imdbclone.person.dto.PersonResponseDto;
import org.example.imdbclone.person.dto.PersonUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonResponseDto savePerson(@RequestBody PersonCreateDto person){
        return personService.createPerson(person);
    }

    @GetMapping
    public List<PersonResponseDto> getAllPersons(){
        return personService.getAllPersons();
    }

    @GetMapping("/{id}")
    public PersonResponseDto getPersonById(@PathVariable Long id){
        return personService.getPersonById(id);
    }

    @PutMapping("/{id}")
    public PersonResponseDto updatePerson(@PathVariable Long id, @PathVariable PersonUpdateDto dto){
        return personService.updatePerson(id, dto);
    }

    @PatchMapping("/{id}")
    public PersonResponseDto patchPerson(@PathVariable Long id, @RequestBody PersonPatchDto dto){
        return personService.patchPerson(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable Long id){
        personService.deletePerson(id);
    }
}
