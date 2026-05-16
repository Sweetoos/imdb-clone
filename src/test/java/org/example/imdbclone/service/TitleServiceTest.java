package org.example.imdbclone.service;

import org.example.imdbclone.model.Title;
import org.example.imdbclone.model.TitleType;
import org.example.imdbclone.repository.TitleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TitleServiceTest {
    @Mock
    private TitleRepository titleRepository;

    @InjectMocks
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
    void shouldCreateTitleSuccessfully() throws Exception {
        Title titleToSave=Title.builder()
                .titleName("50 pozycji Lococka")
                .explicitContent(true)
                .runtimeMinutes(69)
                .startYear(2026)
                .endYear(2026)
                .titleType(TitleType.TVSERIES)
                .build();

        when(titleRepository.save(any(Title.class))).thenReturn(title);

        Title result=titleService.createTitle(titleToSave);

        assertNotNull(result.getTitleId(), "ID should not be null");
        assertEquals(99999999L,result.getTitleId(), "ID should be 99999999");

        assertEquals("50 pozycji Lococka", result.getTitleName());
        verify(titleRepository, times(1)).save(titleToSave);
    }

    @Test
    void shouldReturnTitleByIdSuccessfully() throws Exception {
        when(titleRepository.findById(99999999L)).thenReturn(Optional.of(title));

        Optional<Title> foundTitle=titleService.getTitleById(99999999L);

        assertThat(foundTitle).isPresent();
        assertThat(foundTitle.get().getTitleId()).isEqualTo(99999999L);
    }

    @Test
    void shouldReturnEmptyOptionalWhenTitleNotFound() throws Exception {
        when(titleRepository.findById(99999999L)).thenReturn(Optional.empty());
        Optional<Title> foundTitle=titleService.getTitleById(99999999L);

        assertThat(foundTitle).isEmpty();
        verify(titleRepository, never()).save(any(Title.class));
    }

    @Test
    void shouldUpdateTitleSuccessfully() throws Exception {
        when(titleRepository.findById(99999999L)).thenReturn(Optional.of(title));
        //TODO
    }
}
