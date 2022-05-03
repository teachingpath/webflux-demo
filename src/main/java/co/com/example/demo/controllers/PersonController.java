package co.com.example.demo.controllers;

import co.com.example.demo.business.PersonRepository;
import co.com.example.demo.business.PersonService;
import co.com.example.demo.business.PetRepository;
import co.com.example.demo.business.entities.Pet;
import co.com.example.demo.business.model.RequestPersonModel;
import co.com.example.demo.business.model.ResponsePersonModel;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/api")
public class PersonController {

    private final PersonService services;


    public PersonController(PersonService services) {
        this.services = services;
    }

    @PostMapping
    public Mono<String> save(@RequestBody RequestPersonModel model) {
        return services.save(model);
    }

    @GetMapping
    public Flux<ResponsePersonModel> getAll() {

       return services.getAll();
    }


}
