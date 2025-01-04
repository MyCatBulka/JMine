package com.bulka.java.games.jmine.settings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

public class SettingsManager {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Properties properties;
    private Properties standardProperties;
    public static final String outPath = "settings.properties";
    public static final String inPath = "/settings/settings.properties";

    public SettingsManager() {
        properties = new Properties();
        standardProperties = new Properties();
    }

    public void load(){
        try {
            logger.info("Loading standard settings");
            standardProperties.load(SettingsManager.class.getResourceAsStream(inPath));
            logger.info("Successful loaded standard settings properties file from inner path");
        } catch (Exception e){
            logger.severe("Can`t load standard properties from .jar, terminating start: " + e.getMessage());
            throw new RuntimeException(e);
        }
        try {
            logger.info("Loading settings properties file");
            if(Files.exists(Paths.get(outPath))) {
                properties.load(new BufferedReader(new FileReader(outPath)));
                logger.info("Successful loaded settings properties file from outer path");
            } else {
                throw new FileNotFoundException("Can`t find outer properties " + outPath);
            }
        } catch (Exception e){
            try {
                logger.warning("Can`t load properties from file: " + e.getMessage() + ", using standard from .jar and copying file");
                Files.copy(Objects.requireNonNull(SettingsManager.class.getResourceAsStream(inPath)), Paths.get(outPath));
                logger.info("Successful copied settings file. Loading properties");
                properties.load(new BufferedReader(new FileReader(outPath)));
                logger.info("Successful loaded properties");
            } catch (Exception ex) {
                logger.warning("Can`t copy standard properties to outer properties: " + e.getMessage());
            }
        }
    }

    public void save(){
        logger.info("Saving settings");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outPath));
            properties.store(bw, null);
            bw.close();
            logger.info("Successful saved settings");
        } catch (Exception e) {
            logger.warning("Can`t save settings: " + e.getMessage());
        }
    }

    public String get(String key){
        String out = properties.getProperty(key);
        if(out == null)
            out = standardProperties.getProperty(key);
        return out;
    }
    public String get(String key, Object defaultValue){
        String out = properties.getProperty(key, defaultValue.toString());
        if(out == null)
            out = standardProperties.getProperty(key, defaultValue.toString());
        return out;
    }
    public int getInt(String key){
        return Integer.parseInt(get(key));
    }
    public int getInt(String key, int defaultValue){
        return Integer.parseInt(get(key, String.valueOf(defaultValue)));
    }
    public void set(String key, Object value){
        properties.setProperty(key, value.toString());
    }

    public Properties getProperties() {
        return properties;
    }

    public void destroy(){
    }
}
