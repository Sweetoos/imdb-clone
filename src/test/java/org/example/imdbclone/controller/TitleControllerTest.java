package org.example.imdbclone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.imdbclone.model.Title;
import org.example.imdbclone.model.TitleType;
import org.example.imdbclone.service.TitleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TitleController.class)
@AutoConfigureJsonTesters
public class TitleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper=new ObjectMapper();

    @MockitoBean
    private TitleService titleService;

    private Title title;

    @BeforeEach
    void setUp(){
        title=new Title();
        title.setTitleId(99999999L);
                title.setTitleName("50 pozycji Lococka");
                title.setExplicitContent(true);
                title.setRuntimeMinutes(69);
                title.setStartYear(2026);
                title.setEndYear(2026);
                title.setTitleType(TitleType.TVSERIES);
    }

    @Test
    void shouldCreateTitleViaEndpoint() throws Exception {
        Title titleToCreate = Title.builder()
                .titleName("50 pozycji Lococka")
                .explicitContent(true)
                .runtimeMinutes(69)
                .startYear(2026)
                .endYear(2026)
                .titleType(TitleType.MOVIE)
                .build();


        when(titleService.createTitle(any(Title.class))).thenReturn(title);

        mockMvc.perform(post("/titles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(titleToCreate)).with(csrf())
                        .with(csrf())
                        .with(user("user").roles("USER", "ADMIN")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titleId").value(99999999))
                .andExpect(jsonPath("$.titleName").value("50 pozycji Lococka"));
    }

    @Test
    void shouldReturnTitleViaEndpoint() throws Exception {
        when(titleService.getTitleById(99999999L)).thenReturn(Optional.of(title));

        mockMvc.perform(get("/api/titles/99999999"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.titleId").value(99999999))
                .andExpect(jsonPath("$.titleName").value("50 pozycji Lococka"))
                .andExpect(jsonPath("$.explicitContent").value(true))
                .andExpect(jsonPath("$.runtimeMinutes").value(69))
                .andExpect(jsonPath("$.startYear").value(2026))
                .andExpect(jsonPath("$.titleType").value(TitleType.TVSERIES))
                .andExpect(jsonPath(("$.endYear")).value(2026));
    }

    @Test
    void shouldReturnAllTitleViaEndpoint() throws Exception {
        when(titleService.getAllTitles()).thenReturn(Arrays.asList(title));
    }
}
