package jare.festivalweather.controller;

import jare.festivalweather.entity.Festival;
import jare.festivalweather.repository.FestivalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FestivalController.class)
public class TestFestivalController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FestivalRepository festivalRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetAllFestivals() throws Exception {
        Festival festival = new Festival();
        festival.setName("Test Festival");
        Iterable<Festival> festivals = Collections.singletonList(festival);
        Mockito.when(festivalRepository.findAll()).thenReturn(festivals);

        mockMvc.perform(get("/api/festivals/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Festival"));
    }

    @Test
    public void testGetGenres() throws Exception {
        Festival festival1 = new Festival();
        festival1.setGenres("Rock");
        Festival festival2 = new Festival();
        festival2.setGenres("Jazz");
        Iterable<Festival> festivals = new ArrayList<>(List.of(festival1, festival2));
        Mockito.when(festivalRepository.findAll()).thenReturn(festivals);

        mockMvc.perform(get("/api/festivals/get/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("Rock"))
                .andExpect(jsonPath("$[1]").value("Jazz"));
    }

    @Test
    public void testGetFestivalByName() throws Exception {
        Festival festival = new Festival();
        festival.setName("Test Festival");
        Mockito.when(festivalRepository.findFestivalByName("Test Festival")).thenReturn(Optional.of(festival));

        mockMvc.perform(get("/api/festivals/get/festival/by-name")
                        .param("name", "Test Festival"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Festival"));
    }

    @Test
    public void testAddFestival() throws Exception {
        Festival newFestival = new Festival();
        newFestival.setName("New Festival");
        Mockito.when(festivalRepository.findFestivalByName("New Festival")).thenReturn(Optional.empty());
        Mockito.when(festivalRepository.save(any(Festival.class))).thenReturn(newFestival);

        mockMvc.perform(post("/api/festivals/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Festival\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("true"));
    }

    @Test
    public void testAddFestivalExists() throws Exception {
        Festival existingFestival = new Festival();
        existingFestival.setName("Existing Festival");
        Mockito.when(festivalRepository.findFestivalByName("Existing Festival")).thenReturn(Optional.of(existingFestival));

        mockMvc.perform(post("/api/festivals/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Existing Festival\"}"))
                .andExpect(status().isFound())
                .andExpect(content().string("false"));
    }
}