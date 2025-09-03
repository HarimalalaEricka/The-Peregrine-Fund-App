package com.example.serveur.repository;

import com.example.serveur.model.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserAppRepository extends JpaRepository<UserApp, Long> {
    Optional<UserApp> findByLogin(String login);
    Optional<UserApp> findByLoginAndMotDePasse(String login, String motDePasse);
}