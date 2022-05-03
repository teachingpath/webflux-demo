package co.com.example.demo.business;

import co.com.example.demo.business.entities.Person;
import co.com.example.demo.business.entities.Pet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends ReactiveCrudRepository<Pet, String> {
}
