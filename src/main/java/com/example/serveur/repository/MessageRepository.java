package com.example.serveur.repository;

import com.example.serveur.model.Message;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
     @Query("SELECT COUNT(m) > 0 FROM Message m WHERE " +
           "m.dateCommencement = :dateCommencement AND " +
           "m.dateSignalement = :dateSignalement AND " +
           "m.idIntervention = :idIntervention AND " +
           "m.idUserApp = :idUserApp")
    boolean existsByDetails(
        @Param("dateCommencement") LocalDateTime dateCommencement,
        @Param("dateSignalement") LocalDateTime dateSignalement,
        @Param("idIntervention") Integer idIntervention,
        @Param("idUserApp") Integer idUserApp);
}