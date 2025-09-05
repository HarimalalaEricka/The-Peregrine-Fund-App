package com.example.serveur.repository;

import com.example.serveur.model.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
     @Query("SELECT COUNT(m) > 0 FROM Message m WHERE " +
           "m.longitude = :longitude AND " +
           "m.latitude = :latitude ")
    boolean existsByDetails(
        @Param("longitude") BigDecimal longitude,
        @Param("latitude") BigDecimal latitude);

    // Compter les messages par site
    @Query(value = "SELECT p.id_site, COUNT(m.*)\r\n" + //
                "FROM message m \r\n" + //
                "JOIN UserApp u ON m.iduserapp = u.iduserapp\r\n" + //
                "JOIN Patrouilleurs p ON u.id_patrouilleur = p.id_patrouilleur\r\n" + //
                "GROUP BY p.id_site", nativeQuery = true)
    List<Object[]> countMessagesBySite();
}