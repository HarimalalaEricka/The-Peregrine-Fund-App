package com.example.serveur.repository;

import com.example.serveur.model.HistoriqueMessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueMessageStatusRepository extends JpaRepository<HistoriqueMessageStatus, Integer> {
}