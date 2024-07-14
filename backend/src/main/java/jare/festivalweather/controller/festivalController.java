package jare.festivalweather.controller;

import jare.festivalweather.entity.Festival;
import jare.festivalweather.repository.FestivalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/festivals")
public class festivalController {

    @Autowired
    private FestivalRepository festivalRepository;


    //-----

    @GetMapping("/get")
    public ResponseEntity<Iterable<Festival>> getAllFestivals() {
        return new ResponseEntity<>(festivalRepository.findAll(), HttpStatus.OK);
    }


    @GetMapping("/get/genres")
    public ResponseEntity<ArrayList<String>> getGenres() {
        ArrayList<String> genresList = new ArrayList<>();
        Iterable<Festival> allFestivals = (Iterable<Festival>) getAllFestivals();

        for (Festival f : allFestivals) {
            if (!genresList.contains(f.getGenres())){
                genresList.add(f.getGenres());
            }
        }

        return new ResponseEntity<>(genresList, HttpStatus.OK);
    }





    @PostMapping("/add")
    public ResponseEntity<Boolean> addFestival(@RequestBody Festival newFestival) {
        Optional<Festival> existFestival = festivalRepository.findFestivalByName(newFestival.getName());
        if (!existFestival.isPresent()) {
            festivalRepository.save(newFestival);
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(false, HttpStatus.FOUND);
    }
}
