package jare.festivalweather.repository;

import jare.festivalweather.entity.Festival;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FestivalRepository extends CrudRepository<Festival, String> {

    Optional<Festival> findFestivalByName(String name);

}
