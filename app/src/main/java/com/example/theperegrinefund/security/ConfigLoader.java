package com.example.theperegrinefund.security;

import android.content.Context;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    public static String getSecretKey(Context context) throws Exception {
        Properties props = new Properties();
        try (InputStream input = context.getAssets().open("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Fichier config.properties introuvable !");
            }
            props.load(input);
        }
        return props.getProperty("secret.key");
    }

    // Nouvelle méthode pour récupérer le numéro
    public static String getFixedNumber(Context context) throws Exception {
        Properties props = new Properties();
        try (InputStream input = context.getAssets().open("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Fichier config.properties introuvable !");
            }
            props.load(input);
        }
        return props.getProperty("fixed.number");
    }

    // Nouvelle méthode pour récupérer l'adresse IP ou URL du serveur
    public static String getServerUrl(Context context) throws Exception {
        Properties props = new Properties();
        try (InputStream input = context.getAssets().open("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Fichier config.properties introuvable !");
            }
            props.load(input);
        }
        return props.getProperty("server.url");
    }
}
