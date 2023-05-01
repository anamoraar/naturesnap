package com.p2.naturesnap.repository;
import com.p2.naturesnap.model.Kingdom;
import com.p2.naturesnap.model.Taxon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

//Interfaz que extiende a JpaRepository para realizar operaciones CRUD con los taxones
@Repository
public interface TaxonRepository extends JpaRepository<Taxon, Integer> {

    //Consulta para retornar todos los taxones existentes
    @Query("SELECT t FROM Taxon t")
    List<Taxon> findAll();

    //Consulta para retornar un taxón por su id
    @Query("SELECT t FROM Taxon t WHERE t.id= ?1")
    Optional<Taxon> findTaxonById(int id);

    //Consulta para retornar todos los taxones que tienen un ancestro dado su id
    @Query("SELECT t FROM Taxon t WHERE t.ancestor= ?1")
    List<Taxon> findTaxonsByAncestor(int id);


}
