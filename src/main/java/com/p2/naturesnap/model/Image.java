package com.p2.naturesnap.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity //Indica que esta clase es una entidad en la base de datos
public class Image implements ViewInformation {
    @Id //su primary key es el id, que es autogenerado por la base
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;
    @Column(length = 500) //se pide que tenga un max. de 500 caracteres
    private String description;

    @JsonIgnore //para no solicitar la creation_date y setearla a LocalDate.now()
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate creation_date;

    @ElementCollection //Indica que se debe hacer una tabla extra con los valores de la lista
    private List<String> keywords;

    @ManyToOne //muchas imágenes pueden tener el mismo owner
    private Owner owner;

    @ManyToOne //muchas imágenes pueden tener el mismo autor
    private Person author;

    @ManyToMany //muchas imágenes pueden tener muchos taxones asociados
    @JoinTable(name = "ImageTaxon",
            joinColumns = @JoinColumn(name = "image_id"),
            inverseJoinColumns = @JoinColumn(name = "taxon_id"))
    private List<Taxon> taxons;

    @Enumerated(EnumType.STRING) //Indica que la licencia es un enum
    private License license;

    //El path donde queda guardada la imagen
    private String path;

    @Transient //Indica que este atributo no se debe reflejar en la base de datos
    static int image_counter = 0;

    //Constructor, aumenta el counter cuando se crea una imagen
    public Image(){
        image_counter++;
    }

    //Los respectivos setters y getters
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreation_date() {
        return creation_date;
    }

    @PrePersist //Antes de crear la entidad, va a setear creation_date a LocalDate.now()
    public void setCreation_date() {
        this.creation_date = LocalDate.now();
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public List<Taxon> getTaxons() {
        return taxons;
    }

    public void setTaxons(List<Taxon> taxons) {
        this.taxons = taxons;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //Método de la interfaz, para desplegar las keywords en las vistas
    @Override
    public String displayInformation() {
        int tam = this.keywords.size();
        String palabras = " ";
        for(int i = 0; i < tam-1; i++){
            palabras += keywords.get(i)+", ";
        }
        return palabras+keywords.get(tam-1);
    }
}
