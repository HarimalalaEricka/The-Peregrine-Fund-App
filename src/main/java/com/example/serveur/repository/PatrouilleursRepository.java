package com.example.serveur.repository;

import com.example.serveur.model.Patrouilleurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatrouilleursRepository extends JpaRepository<Patrouilleurs, Integer> {
    
    @Query("SELECT DISTINCT p.telephone FROM Patrouilleurs p WHERE p.role = 'Agent'")
    List<String> findTelephonesByRoleAgent();
    
    // Alternative avec paramètre
    @Query("SELECT DISTINCT p.telephone FROM Patrouilleurs p WHERE p.role = :role")
    List<String> findTelephonesByRole(@Param("role") String role);
}