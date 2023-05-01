package com.p2.naturesnap.model;

import jakarta.persistence.*;

@Entity //Indica que esta clase es persistida como entidad en la base
//Utiliza la estrategia Table Per Class, es decir, en la base hay tablas para cada subclase
//y existe Owner, por lo que no hay problema relacionando otra entidad a Owner
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Owner implements ViewInformation{
    @Id //su primary key es el id, que es autogenerado por la base
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String country;
    private long phone_number;
    private String email;

    //Constructores
    public Owner() {

    }

    public Owner(String name, String country, long phone_number, String email) {
        this.name = name;
        this.country = country;
        this.phone_number = phone_number;
        this.email = email;
    }

    //Los respectivos setters y getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //Método de la interfaz para desplegar el nombre de un owner
    public String displayInformation(){
        return this.getName();
    }

}
