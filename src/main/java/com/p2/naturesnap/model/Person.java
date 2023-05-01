package com.p2.naturesnap.model;

import jakarta.persistence.Entity;

@Entity //Person es una entidad en la base de datos
public class Person extends Owner{
    private String lastName;

    //Constructores
    public Person() {
        super();
    }

    public Person(String name, String country, long phone_number, String email, String lastName) {
        super(name, country, phone_number, email);
        this.lastName = lastName;
    }

    //Setters y getters
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    //Agrega al método de interfaz implementado por owner el apellido de la persona
    @Override
    public String displayInformation() {
        return super.displayInformation()+" "+this.getLastName();
    }
}
