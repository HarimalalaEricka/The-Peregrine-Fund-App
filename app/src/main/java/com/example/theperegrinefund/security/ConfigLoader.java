package com.example.theperegrinefund.security;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final String CONFIG_FILE = "/config.properties";

    public static String getSecretKey() throws Exception {
        Properties props = new Properties();
        try (InputStream input = ConfigLoader.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Fichier config.properties introuvable !");
            }
            props.load(input);
        }
        return props.getProperty("secret.key");
    }
}
