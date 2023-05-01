package com.p2.naturesnap.repository;

import com.p2.naturesnap.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//Interfaz que extiende a JpaRepository para realizar operaciones CRUD con las imágenes
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    //Consulta para retornar todas las imágenes
    @Query("SELECT i FROM Image i")
    List<Image> findAll();

    //Consulta para retornar una imagen por su id
    @Query("SELECT i FROM Image i WHERE i.id= ?1")
    Optional<Image> findImageById(int id);
}
