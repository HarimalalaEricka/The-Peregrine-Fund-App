package com.example.theperegrinefund.repository;

import com.example.theperegrinefund.UserApp;
import com.example.theperegrinefund.Patrouilleur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface UserAppRepository extends JpaRepository<UserApp, Integer> {

    // 🔹 Vérifier si un login existe
    boolean existsByLogin(String login);

    // 🔹 Récupérer un utilisateur par login
    Optional<UserApp> findByLogin(String login);

    // 🔹 Authentification : chercher par login et mot de passe
    Optional<UserApp> findByLoginAndMotDePasse(String login, String motDePasse);

    // 🔹 Vérifier si un patrouilleur a un compte
    boolean existsByAgent(Patrouilleur patrouilleur);

    // 🔹 Exemple avec une query personnalisée (jointure avec Patrouilleur)
    @Query("SELECT u FROM UserApp u JOIN FETCH u.agent p WHERE p.idPatrouilleur = :idPatrouilleur")
    Optional<UserApp> findByPatrouilleurId(@Param("idPatrouilleur") int idPatrouilleur);
}
