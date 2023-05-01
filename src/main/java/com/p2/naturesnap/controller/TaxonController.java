package com.p2.naturesnap.controller;

import com.p2.naturesnap.model.*;
import com.p2.naturesnap.model.Class;
import com.p2.naturesnap.repository.TaxonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

//Clase que responde a las solicitudes HTTP para las acciones relacionadas a los taxones
@RestController
public class TaxonController {

    //Inyección del repositorio para hacer operaciones CRUD con taxones
    @Autowired
    private TaxonRepository taxonRepository;

    //Agrega todos los taxones de la base de datos como objeto al modelView y
    //retorna la vista "taxons" para desplegar todos los taxones del sistema
    @GetMapping("/taxons")
    public ModelAndView showAllTaxons() {
        ModelAndView modelView = new ModelAndView();
        List<Taxon> taxons = taxonRepository.findAll();
        modelView.addObject("taxons", taxons);
        modelView.setViewName("taxons.html");
        return modelView;
    }

    //Retorna la vista que tiene el Form para crear un nuevo taxón
    @GetMapping("/taxons/addtaxon")
    public ModelAndView taxonForm(){
        ModelAndView modelView = new ModelAndView();
        modelView.setViewName("taxonform.html");
        return modelView;
    }

    //Dado un String llamado taxonType, crea un nuevo taxón verificando que su ancestro sea legal
    //y retorna un ResponseEntity para saber si se tuvo éxito (.ok) o hubo un error (.badRequest)
    @PostMapping("/taxons/createtaxon")
    public ResponseEntity<String> createTaxon(@RequestParam String taxonType, @RequestParam String name,
                                              @RequestParam String author, @RequestParam int year, @RequestParam int ancestor){
        //Obtener el ancestro del taxón que se quiere crear
        Optional<Taxon> ancestro = taxonRepository.findTaxonById(ancestor);
        //Formatear la fecha
        LocalDate date = LocalDate.of(year, Month.JANUARY, 1);
        //Dado el taxonType se crea el taxón en el sistema y, en caso de los datos sean válidos, se agrega
        //a la base de datos
        switch (taxonType) {
            case "Kingdom":
                Kingdom kingdom = new Kingdom(name, author, ancestor, date);
                if (kingdom.check_ancestor(ancestro))
                    return ResponseEntity.badRequest().body("Ancestro ilegal.");
                taxonRepository.save(kingdom);
                return ResponseEntity.ok("Kingdom creado.");
            case "Phylum":
                Phylum phylum = new Phylum(name, author, ancestor, date);
                if (phylum.check_ancestor(ancestro))
                    return ResponseEntity.badRequest().body("Ancestro debe ser un Kingdom.");
                taxonRepository.save(phylum);
                return ResponseEntity.ok("Phylum creado.");
            case "Class":
                Class clazz = new Class(name, author, ancestor, date);
                if (clazz.check_ancestor(ancestro))
                    return ResponseEntity.badRequest().body("Ancestro debe ser un Phylum.");
                taxonRepository.save(clazz);
                return ResponseEntity.ok("Class creada.");
            case "Order":
                Order order = new Order(name, author, ancestor, date);
                if (order.check_ancestor(ancestro))
                    return ResponseEntity.badRequest().body("Ancestro debe ser una Class.");
                taxonRepository.save(order);
                return ResponseEntity.ok("Order creada.");
            case "Family":
                Family family = new Family(name, author, ancestor, date);
                if (family.check_ancestor(ancestro))
                    return ResponseEntity.badRequest().body("Ancestro debe ser un Order.");
                taxonRepository.save(family);
                return ResponseEntity.ok("Family creado.");
            case "Genus":
                Genus genus = new Genus(name, author, ancestor, date);
                if (genus.check_ancestor(ancestro))
                    return ResponseEntity.badRequest().body("Ancestro debe ser una Family.");
                taxonRepository.save(genus);
                return ResponseEntity.ok("Genus creado.");
            case "Species":
                Species species = new Species(name, author, ancestor, date);
                if (species.check_ancestor(ancestro))
                    return ResponseEntity.badRequest().body("Ancestro debe ser un Genus.");
                taxonRepository.save(species);
                return ResponseEntity.ok("Species creado.");
        }
        return ResponseEntity.badRequest().body("Error");
    }

    //Recibe el id del taxón que se quiere eliminar y revisa si no es ancestro de otros taxones.
    //De ser así, lo elimina, pero si tiene hijos retorna un error para no eliminarlo
    @DeleteMapping("/taxons/delete/{id}")
    public ResponseEntity<?> deleteTaxon(@PathVariable("id") int id) {
        Optional<Taxon> taxon = taxonRepository.findTaxonById(id);
        List<Taxon> hijos = taxonRepository.findTaxonsByAncestor(id);
        if (taxon.isPresent() && hijos.isEmpty()) {
            taxonRepository.delete(taxon.get());
            return ResponseEntity.ok("Taxon deleted.");
        }
        return ResponseEntity.badRequest().body("Error.");
    }

    //Retorna la vista que tiene el Form para editar un taxón existente, a esta
    //vista se le agrega el id del taxón que se quiere editar
    @GetMapping("/taxons/edit")
    public ModelAndView editTaxonForm(@RequestParam int id){
        ModelAndView modelView = new ModelAndView();
        Optional<Taxon> taxonOptional = taxonRepository.findTaxonById(id);
        if(taxonOptional.isPresent())
            modelView.addObject("id", id);
        modelView.setViewName("edittaxon.html");
        return modelView;
    }

    //Edita un taxón (dado su id) y modifica los atributos que la persona ingresó en el form. Si no hay
    //problemas retorna un ResponseEntity.ok() y de lo contrario retorna un ResponseEntity.badRequest()
    @PutMapping("/taxons/update/{id}")
    public ResponseEntity<?> editTaxon(@PathVariable("id") int id, @RequestParam String name,
                           @RequestParam String author, @RequestParam String year, @RequestParam String ancestor){
        Optional<Taxon> taxonOptional = taxonRepository.findTaxonById(id);
        if(taxonOptional.isPresent()){
            Taxon taxon = taxonOptional.get();
            //Solamente se cambian los atributos que no están vacíos
            if(!name.equals(""))  taxon.setScientific_name(name);
            if(!author.equals("")) taxon.setAuthor(author);
            if(!year.equals("")) taxon.setPublication_year(LocalDate.of(Integer.parseInt(year), Month.JANUARY, 1));
            if(!ancestor.equals("") && !taxon.check_ancestor(taxonRepository.findTaxonById(Integer.parseInt(ancestor))))
                taxon.setAncestor(Integer.parseInt(ancestor));
            taxonRepository.save(taxon);
            return ResponseEntity.ok("Taxon updated");
        }
        return ResponseEntity.badRequest().body("Error");
    }
}