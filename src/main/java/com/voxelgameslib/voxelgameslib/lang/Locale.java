package com.voxelgameslib.voxelgameslib.lang;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nonnull;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Optional;

/**
 * Represents a language a player can select to get all messages in that language
 */
@Data
@AllArgsConstructor
public class Locale implements Serializable {

    public static final Locale ENGLISH = new Locale("english", "en");
    public static final Locale FRENCH = new Locale("french", "fr");
    public static final Locale GERMAN = new Locale("german", "de");
    public static final Locale ITALIAN = new Locale("italian", "it");
    public static final Locale PORTUGUESE = new Locale("portuguese", "pt");
    public static final Locale RUSSIAN = new Locale("russian", "ru");
    public static final Locale SPANISH = new Locale("spanish", "es");
    public static final Locale SWEDISH = new Locale("swedish", "sv");
    public static final Locale TURKISH = new Locale("turkish", "tr");
    public static final Locale DUTCH = new Locale("dutch", "nl");
    public static final Locale BRAZILIAN_PORTUGESE = new Locale("brazilian portuguese", "pt-br");

    private String name;
    @Expose
    @Id
    private String tag;

    protected Locale() {
        // JPA
    }

    /**
     * @return all known locales
     */
    @Nonnull
    public static Locale[] values() {
        return new Locale[]{ENGLISH, FRENCH, GERMAN, ITALIAN, PORTUGUESE, RUSSIAN, SPANISH, SWEDISH,
                TURKISH, DUTCH, BRAZILIAN_PORTUGESE};
    }

    /**
     * Searches for a locale with that tag
     *
     * @param tag the tag to search for
     * @return a optionally found locale
     */
    @Nonnull
    public static Optional<Locale> fromTag(@Nonnull String tag) {
        for (Locale loc : values()) {
            if (loc.getTag().equalsIgnoreCase(tag)) {
                return Optional.of(loc);
            }
        }

        return Optional.empty();
    }

    /**
     * Searches for a locale with that name
     *
     * @param name the name to search for
     * @return a optionally found locale
     */
    @Nonnull
    public static Optional<Locale> fromName(@Nonnull String name) {
        for (Locale loc : values()) {
            if (loc.getName().equalsIgnoreCase(name)) {
                return Optional.of(loc);
            }
        }

        return Optional.empty();
    }
}
