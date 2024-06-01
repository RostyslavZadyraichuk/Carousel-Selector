package com.zadyraichuk.general;

import java.io.*;
import java.util.Properties;

public class PropertiesFile {

    private Properties properties;
    private File file;

    public PropertiesFile(File file) throws IOException {
        loadProperties(file);
    }

    public void loadProperties(File file) throws IOException {
        if (this.file != null) {
            saveProperties();
        }

        this.file = file;
        load();
    }

    public void saveProperties() throws IOException {
        try (OutputStream os = new FileOutputStream(file)) {
            properties.store(os, "");
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    private void load() throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            properties = new Properties();
            properties.load(is);
        }
    }
}