package org.example.imdbclone.rest;

import org.example.imdbclone.repository.TitleRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
//@CrossOrigin("http://localhost:4200")
public class DemoController {
    @GetMapping("/resource")
    public Map<String, Object> home() {
        return Map.of(
                "id", UUID.randomUUID().toString(),
                "content", "Hello From backend Spring Boot"
        );
    }
}
