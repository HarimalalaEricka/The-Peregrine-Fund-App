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
}
