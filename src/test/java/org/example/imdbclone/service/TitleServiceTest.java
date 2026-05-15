package org.example.imdbclone.service;

import org.example.imdbclone.model.Title;
import org.example.imdbclone.model.TitleType;
import org.example.imdbclone.repository.TitleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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

    @Test
    void shouldCreateTitle(){
        Title titleToSave=Title.builder()
                .titleName("50 pozycji Lococka")
                .explicitContent(true)
                .runtimeMinutes(69)
                .startYear(2026)
                .titleType(TitleType.MOVIE)
                .build();

        Title savedTitle=Title.builder()
                .titleId(99999999L)
                .titleName("50 pozycji Lococka")
                .explicitContent(true)
                .runtimeMinutes(69)
                .startYear(2026)
                .titleType(TitleType.MOVIE)
                .build();


        when(titleRepository.save(any(Title.class))).thenReturn(savedTitle);

        Title result=titleService.createTitle(titleToSave);

        assertNotNull(result.getTitleId(), "ID should not be null");
        assertEquals(99999999L,result.getTitleId(), "ID should be 99999999");

        assertEquals("50 pozycji Lococka", result.getTitleName());
        verify(titleRepository, times(1)).save(titleToSave);
    }
}
