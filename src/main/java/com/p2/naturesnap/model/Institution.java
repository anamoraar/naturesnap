package com.p2.naturesnap.model;

import jakarta.persistence.Entity;

@Entity //Indica que Institution es una entidad en la base de datos
public class Institution extends Owner{
    private String website;

    //Constructores
    public Institution() {
        super();
    }

    public Institution(String name, String country, long phone_number, String email, String website) {
        super(name, country, phone_number, email);
        this.website = website;
    }

    //Setters y getters
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
