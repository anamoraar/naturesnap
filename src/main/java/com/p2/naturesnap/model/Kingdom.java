package com.p2.naturesnap.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;
import java.util.Optional;

@Entity //Indica que esta clase es una entidad
@DiscriminatorValue("kingdom") //Se diferencia de los otros taxones con este nombre
public class Kingdom extends Taxon{
    //Constructores
    public Kingdom() {
        super();
    }

    public Kingdom(String scientific_name, String author, int ancestor, LocalDate publication_year) {
        super(scientific_name, author, ancestor, publication_year);
    }

    //Método abstracto que deben implementar todos los niveles de taxones para revisar que el ancestro sea legal
    @Override
    public boolean check_ancestor(Optional<Taxon> ancestor) {
        return ancestor.isPresent();
    }
}
