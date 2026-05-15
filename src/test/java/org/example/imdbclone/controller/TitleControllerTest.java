package org.example.imdbclone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.imdbclone.model.Title;
import org.example.imdbclone.model.TitleType;
import org.example.imdbclone.service.TitleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TitleController.class)
@AutoConfigureJsonTesters
public class TitleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper=new ObjectMapper();

    @MockitoBean
    private TitleService titleService;

    @Test
    void shouldCreateTitleViaEndpoint() throws Exception {
        Title titleToCreate = Title.builder()
                .titleName("50 pozycji Lococka")
                .explicitContent(true)
                .runtimeMinutes(69)
                .startYear(2026)
                .titleType(TitleType.MOVIE)
                .build();

        Title titleToSave = Title.builder()
                .titleId(99999999L)
                .titleName("50 pozycji Lococka")
                .explicitContent(true)
                .runtimeMinutes(69)
                .startYear(2026)
                .titleType(TitleType.MOVIE)
                .build();

        when(titleService.createTitle(any(Title.class))).thenReturn(titleToSave);

        mockMvc.perform(post("/titles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(titleToCreate)).with(csrf())
                        .with(csrf())
                        .with(user("user").roles("USER", "ADMIN")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titleId").value(99999999))
                .andExpect(jsonPath("$.titleName").value("50 pozycji Lococka"));
    }
}
