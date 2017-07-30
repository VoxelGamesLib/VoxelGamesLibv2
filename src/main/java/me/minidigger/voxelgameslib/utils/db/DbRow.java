package me.minidigger.voxelgameslib.utils.db;

import java.util.HashMap;

/**
 * TypeDef alias for results with a template return type getter so casting/implicit getInt type
 * calls are not needed.
 */
public class DbRow extends HashMap<String, Object> {
    /**
     * Get the result as proper type. <p> VALID: Long myLong = row.get("someUnsignedIntColumn");
     * INVALID: String myString = row.get("someUnsignedIntColumn");
     *
     * @return Object of the matching type of the result.
     */
    public <T> T get(String column) {
        return (T) super.get(column);
    }

    /**
     * Get the result as proper type., returning default if not found. <p> VALID: Long myLong =
     * row.get("someUnsignedIntColumn"); INVALID: String myString = row.get("someUnsignedIntColumn");
     *
     * @return Object of the matching type of the result.
     */
    public <T> T get(String column, T def) {
        T res = (T) super.get(column);
        if (res == null) {
            return def;
        }
        return res;
    }

    /**
     * Removes a result, returning as proper type. <p> VALID: Long myLong =
     * row.remove("someUnsignedIntColumn"); INVALID: String myString =
     * row.remove("someUnsignedIntColumn");
     *
     * @return Object of the matching type of the result.
     */
    public <T> T remove(String column) {
        return (T) super.remove(column);
    }

    /**
     * Removes a result, returning as proper type, returning default if not found <p> VALID: Long
     * myLong = row.get("someUnsignedIntColumn"); INVALID: String myString =
     * row.get("someUnsignedIntColumn");
     *
     * @return Object of the matching type of the result.
     */
    public <T> T remove(String column, T def) {
        T res = (T) super.remove(column);
        if (res == null) {
            return def;
        }
        return res;
    }

    public DbRow clone() {
        DbRow row = new DbRow();
        row.putAll(this);
        return row;
    }
}
