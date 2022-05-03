package co.com.example.demo.business.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Pet {
    @Id
    private String id;
    private String name;

    public Pet(){

    }

    public Pet(String petName) {
        this.name = petName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
