package com.p2.naturesnap.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

@Entity //Indica que es una entidad
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //Estrategia de herencia para la base de datos
@DiscriminatorColumn(name = "taxon_type") //Nombre del atributo discriminador de los taxones
public abstract class Taxon implements ViewInformation {
    //El id es la PK y la base los genera automáticamente
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;
    private String scientific_name;
    private String author;
    private int ancestor;
    private LocalDate publication_year;

    //Constructores
    public Taxon(){

    }

    public Taxon(String scientific_name, String author, int ancestor, LocalDate publication_year) {
        this.scientific_name = scientific_name;
        this.author = author;
        this.ancestor = ancestor;
        this.publication_year = publication_year;
    }

    //Respectivos setters y getters
    public int getId() {
        return id;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublication_year() {
        return publication_year;
    }

    public void setPublication_year(LocalDate publication_year) {
        this.publication_year = publication_year;
    }

    public int getAncestorID() {
        return this.ancestor;
    }

    public void setAncestor(int id) {
        this.ancestor = id;
    }

    public abstract boolean check_ancestor(Optional<Taxon> ancestor);

    //Método de la interfaz para desplegar solamente el año de creación
    @Override
    public String displayInformation(){
        String fecha = this.getPublication_year().toString();
        return fecha.substring(0, 4);
    }
}
