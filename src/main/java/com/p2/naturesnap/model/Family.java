package com.p2.naturesnap.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;
import java.util.Optional;

@Entity //Esta clase es una entidad
@DiscriminatorValue("family") //Se diferencia de los otros taxones con este nombre
public class Family extends Taxon{
    //Constructores
    public Family() {
        super();
    }

    public Family(String scientific_name, String author, int ancestor, LocalDate publication_year) {
        super(scientific_name, author, ancestor, publication_year);
    }

    //Método abstracto que deben implementar todos los niveles de taxones para revisar que el ancestro sea legal
    @Override
    public boolean check_ancestor(Optional<Taxon> ancestor) {
        return ancestor.isEmpty() || !(ancestor.get() instanceof Order);
    }
}
