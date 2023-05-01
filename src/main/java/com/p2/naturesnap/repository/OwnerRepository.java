package com.p2.naturesnap.repository;

import com.p2.naturesnap.model.Owner;
import com.p2.naturesnap.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//Interfaz que extiende a JpaRepository para realizar operaciones CRUD con los owners
@Repository
public interface OwnerRepository extends JpaRepository<Owner,Integer> {

    //Query para retornar todos los owners de la base de datos
    @Query("SELECT o FROM Owner o")
    List<Owner> findAll();

    //Query para retornar un Owner por su id
    @Query("SELECT o FROM Owner o WHERE o.id= ?1")
    Optional<Owner> findOwnerById(int id);


}
