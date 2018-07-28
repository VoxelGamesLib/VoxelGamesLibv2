package com.voxelgameslib.voxelgameslib.lang;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.persistence.Id;

/**
 * Represents a language a player can select to get all messages in that language
 */
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
    public static final Locale BRAZILIAN_PORTUGUESE = new Locale("brazilian portuguese", "pt-br");

    private String name;
    @Expose
    @Id
    private String tag;

    protected Locale() {
        // JPA
    }

    @java.beans.ConstructorProperties({"name", "tag"})
    public Locale(String name, String tag) {
        this.name = name;
        this.tag = tag;
    }

    /**
     * @return all known locales
     */
    @Nonnull
    public static Locale[] values() {
        return new Locale[]{ENGLISH, FRENCH, GERMAN, ITALIAN, PORTUGUESE, RUSSIAN, SPANISH, SWEDISH,
                TURKISH, DUTCH, BRAZILIAN_PORTUGUESE};
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

    public String getName() {
        return this.name;
    }

    public String getTag() {
        return this.tag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Locale)) return false;
        final Locale other = (Locale) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$tag = this.getTag();
        final Object other$tag = other.getTag();
        if (this$tag == null ? other$tag != null : !this$tag.equals(other$tag)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $tag = this.getTag();
        result = result * PRIME + ($tag == null ? 43 : $tag.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Locale;
    }

    public String toString() {
        return "Locale(name=" + this.getName() + ", tag=" + this.getTag() + ")";
    }
}
