package com.bulka.java.games.jmine.localization;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.settings.SettingsManager;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class LocalizationManager {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private ResourceBundle bundle;
    private Locale locale;
    private final String baseName = "localization/game";
    public static final ResourceBundle.Control UTF8_CONTROL = new UTF8Control();

    public LocalizationManager(){

    }

    public void load(){
        if(SettingsManager.getSelf().get("game.language") != null) {
            locale = new Locale(SettingsManager.getSelf().get("game.language"));
        }
        else {
            locale = Locale.getDefault();
        }
        logger.info("Loading localization: " + locale.getDisplayName());
        try {
            bundle = ResourceBundle.getBundle(baseName, locale, UTF8_CONTROL);
            logger.info("Successful loaded localization " + locale.getDisplayName());
        } catch (MissingResourceException e){
            logger.info("Can`t load localization: " + locale.getDisplayName() + " (missing resource). Loading English");
            try {
                locale = new Locale("en");
                bundle = ResourceBundle.getBundle(baseName, locale, UTF8_CONTROL);
                logger.info("Successful loaded localization " + locale.getDisplayName());
            } catch (Exception ex) {
                logger.info("Can`t load English localization: " + ex.getMessage() + ". Terminating start");
                throw new RuntimeException(ex);
            }

        }
    }

    public String get(String key){
        String out = bundle.getString(key);
        if(!out.isEmpty())
            return out;
        else
            return key;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void destroy(){

    }

    public static LocalizationManager getSelf(){
        return Engine.getEngine().getLocalizationManager();
    }
}
