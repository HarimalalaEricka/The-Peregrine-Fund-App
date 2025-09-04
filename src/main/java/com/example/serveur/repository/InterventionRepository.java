package com.example.serveur.repository;

import com.example.serveur.model.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long> {
    @Query("SELECT i FROM Intervention i WHERE i.typeIntervention = :type")
    Intervention findByType(@Param("type") String type);
}