package com.p2.naturesnap.controller;

import com.p2.naturesnap.model.Owner;
import com.p2.naturesnap.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//Clase que responde a las solicitudes HTTP para las acciones relacionadas a los owners
@RestController
public class OwnerController {

    //Se inyecta el repositorio para realizar las operaciones CRUD con los owners
    @Autowired
    private OwnerRepository ownerRepository;

    //Agrega todos los owners del sistema al modelView y los despliega mediante la vista "owners"
    @GetMapping("/owners")
    public ModelAndView showAllImages() {
        ModelAndView modelView = new ModelAndView();
        List<Owner> owners = ownerRepository.findAll();
        modelView.addObject("owners", owners);
        modelView.setViewName("owners.html");
        return modelView;
    }
}
