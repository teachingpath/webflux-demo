package co.com.example.demo.business;

import co.com.example.demo.business.entities.Contact;
import co.com.example.demo.business.entities.Person;
import co.com.example.demo.business.entities.Pet;
import co.com.example.demo.business.model.RequestPersonModel;
import co.com.example.demo.business.model.ResponsePersonModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PetRepository petRepository;

    public PersonService(PersonRepository personRepository, PetRepository petRepository){

        this.personRepository = personRepository;
        this.petRepository = petRepository;
    }

    public Mono<String> say(String msn){
        return Mono.just(msn);
    }

    public Mono<String> save(RequestPersonModel personModel){
        return Mono.just(personModel)
                .flatMap(modelToEntity())
                .flatMap(personRepository::save)
                .map(Person::getId);
    }



    private Function<RequestPersonModel, Mono<Person>> modelToEntity() {
        var n = 4;
        var s = 0;
        if(n > 3){
            s = s +n;
        }
        System.out.println(s);


        BiFunction<Integer, Integer, Mono<Integer>> fn = (Integer n1, Integer s1) ->
                Mono.just(n1).filter(num -> num > 3).map(num -> s1 + num);

        fn.apply(4,0).subscribe(System.out::println);




        return model -> {
            //TODO: validaciones

            var entity = new Person();
            entity.setContact(new Contact());
            entity.getContact().setAddress(model.getAddress());
            entity.getContact().setPhone(model.getPhone());
            entity.setName(model.getName());

            return Mono.zip(Mono.just(entity), petRepository.save(new Pet(model.getPetName())))
                    .flatMap(objects -> {
                        var person = objects.getT1();
                        var myPet = objects.getT2();
                        person.setPets(new ArrayList<>());
                        person.getPets().add(myPet.getId());
                        return Mono.just(person);
                    });
        };
    }

    public Flux<ResponsePersonModel> getAll() {
        return personRepository.findAll().map(person -> {
            //parte 1
            var response = new ResponsePersonModel();
            response.setContact(person.getContact());
            response.setId(person.getId());
            response.setName(person.getName());
            var pets = person.getPets().stream().map(id -> {
                var mypet = new ResponsePersonModel.Pet();
                mypet.setId(id);
                return mypet;
            }).collect(Collectors.toList());
            response.setPets(pets);
            return response;
        }).flatMap(responsePersonModel ->
                Flux.just(responsePersonModel).zipWith(
                        petRepository
                                .findAllById(responsePersonModel
                                        .getPets().stream().map(ResponsePersonModel.Pet::getId)
                                        .collect(Collectors.toList())
                                ).collectList()

                        , (response, pets) -> {

                            List<ResponsePersonModel.Pet> petList = pets.stream().map(p -> {
                                var mypet = new ResponsePersonModel.Pet();
                                mypet.setId(p.getId());
                                mypet.setName(p.getName());
                                return mypet;
                            }).collect(Collectors.toList());

                            response.setPets(petList);
                            return response;
                        })
        );
    }
}
