package com.voxelgameslib.voxelgameslib.lang;

import com.google.inject.name.Named;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import com.voxelgameslib.voxelgameslib.exception.LangException;
import com.voxelgameslib.voxelgameslib.utils.OrderedProperties;

import lombok.extern.java.Log;

/**
 * A lang storage holds all lang keys (and their translations) that are translated into one locale
 */
@Log
public class LangStorage {

    @Inject
    @Named("LangFolder")
    private File langFolder;

    @Inject
    private LangHandler handler;

    private File langFile;

    private Locale locale;
    private final OrderedProperties messages = new OrderedProperties();
    @Nullable
    private LangStorage parentStorage;

    /**
     * @return the local that the keys in this storage are translated with
     */
    @Nonnull
    public Locale getLocale() {
        return locale;
    }

    /**
     * sets the locale (and with that, the lang file) for this storage
     *
     * @param locale the new locale to set
     */
    public void setLocale(@Nonnull Locale locale) {
        this.locale = locale;
        langFile = new File(langFolder, locale.getTag() + ".properties");
    }

    /**
     * sets the parent storage for this storage. the parent storage is used to get keys that are not
     * translated in this storage.
     *
     * @param parentStorage the new parent storage
     */
    public void setParentStorage(@Nonnull LangStorage parentStorage) {
        this.parentStorage = parentStorage;
    }

    /**
     * Saves the default values for the local that this storage is using to the lang file.
     */
    public void saveDefaultValue() {
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        for (LangKey key : LangKey.values()) {
            messages.setProperty(key.name(), key.getDefaultValue());
        }
        try {
            messages.store(new FileWriter(langFile),
                    "This is a command. I don't really know what this is supposed to do, but lets see!\nLets throw in\nsome newlines!");
        } catch (IOException e) {
            throw new LangException(
                    "Error while saving default lang values to " + langFile.getAbsolutePath(), e);
        }
    }

    /**
     * Adds new values to the lang file, if there where any new added with a update.
     *
     * @return the amount of new values that have been added.
     */
    public int processNewValues() {
        int counter = 0;
        for (LangKey key : LangKey.values()) {
            if (!messages.containsProperty(key.name())) {
                counter++;
                messages.setProperty(key.name(), key.getDefaultValue());
            }
        }

        if (counter > 0) {
            try {
                messages.store(new FileWriter(langFile),
                        "This is a command. I don't really know what this is supposed to do, but lets see!\nLets throw in\nsome newlines!");
            } catch (IOException e) {
                throw new LangException(
                        "Error while saving default lang values to " + langFile.getAbsolutePath(), e);
            }
        }

        return counter;
    }

    /**
     * Tries to load the messages from the langFolder
     *
     * @throws LangException if something goes wrong while loading
     */
    public void load() {
        if (!langFile.exists()) {
            log.info(
                    "Lang file " + langFile.getAbsolutePath() + " does not exist, saving default values");
            saveDefaultValue();
        }
        try {
            messages.load(new FileInputStream(langFile));
        } catch (IOException e) {
            throw new LangException("Could not find lang file for locale" + locale, e);
        }
    }

    /**
     * Gets the value for a key. it this storage does not have a translation for that key, the
     * parent storage is used. If parent has no translation for that key, the default value is
     * used.
     *
     * @param key the key that should be translated
     * @return the translation for that key
     */
    @Nonnull
    public String get(@Nonnull LangKey key) {
        String message = messages.getProperty(key.name());
        if (message == null || message.length() < 2) {
            if (parentStorage != null) {
                message = parentStorage.get(key);
            }
        }

        if (message == null || message.length() < 2) {
            log.log(Level.WARNING, "Could not find value for lang key " + key.name());
            return key.getDefaultValue();
        }

        return message;
    }

    /**
     * @return the file that this storage saves its keys in
     */
    @Nonnull
    public File getLangFile() {
        return langFile;
    }
}
