package com.example.serveur.repository;

import com.example.serveur.model.HistoriqueMessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoriqueMessageStatusRepository extends JpaRepository<HistoriqueMessageStatus, Integer> {
      @Query("SELECT h FROM HistoriqueMessageStatus h WHERE h.message.userApp.idUserApp = :idUserApp")
    List<HistoriqueMessageStatus> findByUserAppId(@Param("idUserApp") int idUserApp);

}