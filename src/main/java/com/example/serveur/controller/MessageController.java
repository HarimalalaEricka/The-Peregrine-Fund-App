package com.example.serveur.controller;

import com.example.serveur.model.Message;
import com.example.serveur.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    // Récupérer les messages d’un utilisateur précis
    @GetMapping("/{idUser}")
    public List<Message> getMessages(@PathVariable int idUser) {
        // On utilise une méthode personnalisée du repository
        return messageRepository.findByUserApp_IdUserApp(idUser);
    }
}
