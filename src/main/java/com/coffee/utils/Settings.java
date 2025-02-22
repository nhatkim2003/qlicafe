package com.coffee.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Settings {
    public static final String DATABASE_FILE = "settings/db.properties";

    public static void initialize() {
        try {
            Files.createDirectories(Paths.get(Resource.getResourcePath("settings", false)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        Resource.copyResource(DATABASE_FILE, DATABASE_FILE);
    }
}
